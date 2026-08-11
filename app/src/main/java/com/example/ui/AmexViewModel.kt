package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AmexRepository
import com.example.data.BenefitInfo
import com.example.data.CardEntity
import com.example.data.ChatMessageEntity
import com.example.data.DiningPlace
import com.example.data.GeminiAiService
import com.example.data.LoungeInfo
import com.example.data.OfferEntity
import com.example.data.PaymentEntity
import com.example.data.TransactionEntity
import com.example.data.TravelTrip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME, CARDS, REWARDS, TRAVEL, MORE
}

enum class MoreSubTab {
    MENU, PAYMENTS, OFFERS, DINING, FINANCIAL_HEALTH, BENEFITS, SECURITY, ASSISTANT, SEARCH
}

sealed class DialogState {
    data object None : DialogState()
    data class PayStatement(val card: CardEntity) : DialogState()
    data class CardDetails(val card: CardEntity) : DialogState()
    data class RequestReplacementCard(val card: CardEntity) : DialogState()
    data class RedeemReward(val title: String, val pointsRequired: Int) : DialogState()
    data class BookDining(val restaurant: DiningPlace, val slot: String) : DialogState()
    data class LoungeQr(val lounge: LoungeInfo) : DialogState()
    data class LockConfirm(val card: CardEntity) : DialogState()
}

class AmexViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AmexRepository(application)
    private val aiService = GeminiAiService()

    val cards: StateFlow<List<CardEntity>> = repository.allCards.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val offers: StateFlow<List<OfferEntity>> = repository.allOffers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val payments: StateFlow<List<PaymentEntity>> = repository.allPayments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val lounges: List<LoungeInfo> = repository.getStaticLounges()
    val diningPlaces: List<DiningPlace> = repository.getStaticDiningPlaces()
    val upcomingTrip: TravelTrip = repository.getUpcomingTrip()
    val benefits: List<BenefitInfo> = repository.getBenefitsList()

    private val _isBusinessMode = MutableStateFlow(false)
    val isBusinessMode: StateFlow<Boolean> = _isBusinessMode.asStateFlow()

    private val _selectedTab = MutableStateFlow(MainTab.HOME)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _moreSubTab = MutableStateFlow(MoreSubTab.MENU)
    val moreSubTab: StateFlow<MoreSubTab> = _moreSubTab.asStateFlow()

    private val _selectedCardIndex = MutableStateFlow(0)
    val selectedCardIndex: StateFlow<Int> = _selectedCardIndex.asStateFlow()

    private val _activeDialog = MutableStateFlow<DialogState>(DialogState.None)
    val activeDialog: StateFlow<DialogState> = _activeDialog.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    fun setBusinessMode(enabled: Boolean) {
        _isBusinessMode.value = enabled
        // Switch card index if current card type doesn't match
        val currentCards = cards.value
        val targetType = if (enabled) "BUSINESS" else "PERSONAL"
        val idx = currentCards.indexOfFirst { it.cardType == targetType }
        if (idx >= 0) {
            _selectedCardIndex.value = idx
        }
    }

    fun setSelectedTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun setMoreSubTab(subTab: MoreSubTab) {
        _moreSubTab.value = subTab
    }

    fun setSelectedCardIndex(index: Int) {
        if (index in cards.value.indices) {
            _selectedCardIndex.value = index
        }
    }

    fun setDialog(dialog: DialogState) {
        _activeDialog.value = dialog
    }

    fun dismissDialog() {
        _activeDialog.value = DialogState.None
    }

    fun setToast(message: String?) {
        _toastMessage.value = message
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleCardLock(cardId: String, isLocked: Boolean) {
        viewModelScope.launch {
            repository.toggleCardLock(cardId, isLocked)
            setToast(if (isLocked) "Card locked instantly" else "Card unlocked successfully")
        }
    }

    fun saveOffer(offerId: String, isSaved: Boolean) {
        viewModelScope.launch {
            repository.saveOffer(offerId, isSaved)
            setToast(if (isSaved) "Offer saved to card!" else "Offer removed")
        }
    }

    fun makePayment(cardId: String, amount: Double, paymentMethod: String) {
        viewModelScope.launch {
            repository.makePayment(cardId, amount, paymentMethod)
            dismissDialog()
            setToast("Simulated payment of $${String.format("%.2f", amount)} completed successfully!")
        }
    }

    fun redeemReward(title: String, pointsRequired: Int) {
        viewModelScope.launch {
            dismissDialog()
            setToast("Redeemed $title for $pointsRequired Points!")
        }
    }

    fun bookDining(restaurant: DiningPlace, slot: String) {
        viewModelScope.launch {
            dismissDialog()
            setToast("Resy Table confirmed at ${restaurant.name} for $slot!")
        }
    }

    fun requestReplacementCard(card: CardEntity) {
        viewModelScope.launch {
            dismissDialog()
            setToast("Replacement card requested for ${card.name}. Express delivery in 24 hrs.")
        }
    }

    fun askAiAssistant(question: String) {
        if (question.isBlank()) return
        viewModelScope.launch {
            repository.addChatMessage("user", question)
            _isAiThinking.value = true
            val currentCards = cards.value
            val currentTxns = transactions.value
            val currentOffers = offers.value

            val reply = aiService.queryAmexIntelligence(
                userPrompt = question,
                cards = currentCards,
                transactions = currentTxns,
                offers = currentOffers
            )
            repository.addChatMessage("assistant", reply)
            _isAiThinking.value = false
        }
    }

    fun clearAiChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }
}
