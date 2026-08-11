package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AmexViewModel
import com.example.ui.DialogState
import com.example.ui.MainTab
import com.example.ui.MoreSubTab
import com.example.ui.components.BottomNav
import com.example.ui.components.TopBar
import com.example.ui.screens.AmexIntelligenceScreen
import com.example.ui.screens.CardsScreen
import com.example.ui.screens.DiningScreen
import com.example.ui.screens.FinancialHealthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreMenuScreen
import com.example.ui.screens.NotificationsSheet
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.PaymentsScreen
import com.example.ui.screens.PurchaseProtectionScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.TravelScreen
import com.example.ui.screens.UniversalSearchSheet
import com.example.ui.theme.AmexGold
import com.example.ui.theme.AmexNavy
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: AmexViewModel = viewModel()
                val context = LocalContext.current

                // Observe Toast messages
                val toastMessage by viewModel.toastMessage.collectAsState()
                LaunchedEffect(toastMessage) {
                    toastMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        viewModel.setToast(null)
                    }
                }

                AmexAppShell(viewModel)
            }
        }
    }
}

@Composable
fun AmexAppShell(viewModel: AmexViewModel) {
    val cards by viewModel.cards.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val offers by viewModel.offers.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    val selectedTab by viewModel.selectedTab.collectAsState()
    val moreSubTab by viewModel.moreSubTab.collectAsState()
    val isBusinessMode by viewModel.isBusinessMode.collectAsState()
    val selectedCardIndex by viewModel.selectedCardIndex.collectAsState()
    val activeDialog by viewModel.activeDialog.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showNotifications by remember { mutableStateOf(false) }
    var showSearchSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                isBusinessMode = isBusinessMode,
                onModeToggle = { viewModel.setBusinessMode(it) },
                onSearchClick = { showSearchSheet = true },
                onNotificationsClick = { showNotifications = true },
                onAssistantClick = {
                    viewModel.setSelectedTab(MainTab.MORE)
                    viewModel.setMoreSubTab(MoreSubTab.ASSISTANT)
                }
            )
        },
        bottomBar = {
            BottomNav(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    viewModel.setSelectedTab(tab)
                    if (tab == MainTab.MORE) {
                        viewModel.setMoreSubTab(MoreSubTab.MENU)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main tab content switcher
            when (selectedTab) {
                MainTab.HOME -> {
                    HomeScreen(
                        cards = cards,
                        selectedCardIndex = selectedCardIndex,
                        onCardSelect = { viewModel.setSelectedCardIndex(it) },
                        transactions = transactions,
                        offers = offers,
                        trip = viewModel.upcomingTrip,
                        diningPlaces = viewModel.diningPlaces,
                        onPayClick = { viewModel.setDialog(DialogState.PayStatement(it)) },
                        onSendClick = {
                            viewModel.setSelectedTab(MainTab.MORE)
                            viewModel.setMoreSubTab(MoreSubTab.PAYMENTS)
                        },
                        onRewardsClick = { viewModel.setSelectedTab(MainTab.REWARDS) },
                        onNavigateTab = { viewModel.setSelectedTab(it) },
                        onNavigateMoreSubTab = { sub ->
                            viewModel.setSelectedTab(MainTab.MORE)
                            viewModel.setMoreSubTab(sub)
                        },
                        onSaveOffer = { id, saved -> viewModel.saveOffer(id, saved) }
                    )
                }
                MainTab.CARDS -> {
                    CardsScreen(
                        cards = cards,
                        selectedCardIndex = selectedCardIndex,
                        onCardSelect = { viewModel.setSelectedCardIndex(it) },
                        transactions = transactions,
                        isBusinessMode = isBusinessMode,
                        onToggleLock = { id, locked -> viewModel.toggleCardLock(id, locked) },
                        onViewDetailsClick = { viewModel.setDialog(DialogState.CardDetails(it)) },
                        onPayClick = { viewModel.setDialog(DialogState.PayStatement(it)) },
                        onRequestReplacementClick = { viewModel.setDialog(DialogState.RequestReplacementCard(it)) }
                    )
                }
                MainTab.REWARDS -> {
                    RewardsScreen(
                        cards = cards,
                        onRedeemRewardClick = { title, points -> viewModel.setDialog(DialogState.RedeemReward(title, points)) }
                    )
                }
                MainTab.TRAVEL -> {
                    TravelScreen(
                        trip = viewModel.upcomingTrip,
                        lounges = viewModel.lounges,
                        onLoungePassClick = { viewModel.setDialog(DialogState.LoungeQr(it)) }
                    )
                }
                MainTab.MORE -> {
                    // Under MORE, we can have sub-screens
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (moreSubTab != MoreSubTab.MENU) {
                            // Back-to-menu header row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable { viewModel.setMoreSubTab(MoreSubTab.MENU) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = AmexNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Back to More Services",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmexNavy
                                )
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            when (moreSubTab) {
                                MoreSubTab.MENU -> {
                                    MoreMenuScreen(
                                        onSubTabSelected = { viewModel.setMoreSubTab(it) }
                                    )
                                }
                                MoreSubTab.PAYMENTS -> {
                                    PaymentsScreen(
                                        cards = cards,
                                        payments = payments,
                                        onPayStatementClick = { viewModel.setDialog(DialogState.PayStatement(it)) }
                                    )
                                }
                                MoreSubTab.OFFERS -> {
                                    OffersScreen(
                                        offers = offers,
                                        onSaveOffer = { id, saved -> viewModel.saveOffer(id, saved) }
                                    )
                                }
                                MoreSubTab.DINING -> {
                                    DiningScreen(
                                        diningPlaces = viewModel.diningPlaces,
                                        onBookSlotClick = { place, slot -> viewModel.setDialog(DialogState.BookDining(place, slot)) }
                                    )
                                }
                                MoreSubTab.FINANCIAL_HEALTH -> {
                                    FinancialHealthScreen(
                                        cards = cards,
                                        transactions = transactions
                                    )
                                }
                                MoreSubTab.BENEFITS -> {
                                    PurchaseProtectionScreen(
                                        benefits = viewModel.benefits
                                    )
                                }
                                MoreSubTab.SECURITY -> {
                                    SecurityCenterScreen()
                                }
                                MoreSubTab.ASSISTANT -> {
                                    AmexIntelligenceScreen(
                                        messages = chatMessages,
                                        isThinking = isAiThinking,
                                        onSendMessage = { viewModel.askAiAssistant(it) },
                                        onClearChat = { viewModel.clearAiChat() }
                                    )
                                }
                                MoreSubTab.SEARCH -> {
                                    UniversalSearchSheet(
                                        query = searchQuery,
                                        onQueryChange = { viewModel.setSearchQuery(it) },
                                        cards = cards,
                                        transactions = transactions,
                                        offers = offers,
                                        lounges = viewModel.lounges,
                                        diningPlaces = viewModel.diningPlaces,
                                        benefits = viewModel.benefits,
                                        onClose = { viewModel.setMoreSubTab(MoreSubTab.MENU) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sheets or Modals
            if (showNotifications) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationsSheet(onClose = { showNotifications = false })
                }
            }

            if (showSearchSheet) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UniversalSearchSheet(
                        query = searchQuery,
                        onQueryChange = { viewModel.setSearchQuery(it) },
                        cards = cards,
                        transactions = transactions,
                        offers = offers,
                        lounges = viewModel.lounges,
                        diningPlaces = viewModel.diningPlaces,
                        benefits = viewModel.benefits,
                        onClose = { showSearchSheet = false }
                    )
                }
            }

            // Custom Dialog Overlays
            when (val state = activeDialog) {
                is DialogState.None -> {}
                is DialogState.PayStatement -> {
                    var inputAmount by remember { mutableStateOf(String.format("%.2f", state.card.paymentDue)) }
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text("Pay Statement", fontWeight = FontWeight.Bold) },
                        text = {
                            Column {
                                Text("Pay from linked checking account Chase •••• 9920")
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = inputAmount,
                                    onValueChange = { inputAmount = it },
                                    label = { Text("Payment Amount ($)") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Outstanding Statement: $${String.format("%.2f", state.card.paymentDue)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val amt = inputAmount.toDoubleOrNull() ?: state.card.paymentDue
                                    viewModel.makePayment(state.card.id, amt, "Chase •••• 9920")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Complete Payment", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                is DialogState.CardDetails -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text(state.card.name, fontWeight = FontWeight.Bold) },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Card Number:", fontWeight = FontWeight.Bold)
                                    Text(state.card.cardNumberMasked.replace("•••• •••• ••••", "3782 8274 9182"))
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Expiry Date:", fontWeight = FontWeight.Bold)
                                    Text("08/29")
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("CVV:", fontWeight = FontWeight.Bold)
                                    Text("2003")
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Current Balance:", fontWeight = FontWeight.Bold)
                                    Text("$${String.format("%.2f", state.card.balance)}")
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Available Credit:", fontWeight = FontWeight.Bold)
                                    Text("$${String.format("%.2f", state.card.availableCredit)}")
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.dismissDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Close", color = Color.White)
                            }
                        }
                    )
                }
                is DialogState.RequestReplacementCard -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text("Request Replacement Card", fontWeight = FontWeight.Bold) },
                        text = {
                            Text("Confirm requesting a replacement card for your ${state.card.name}. Your current card will be disabled and an express replacement will be shipped within 24 hours.")
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.requestReplacementCard(state.card) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Confirm Request", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                is DialogState.RedeemReward -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text("Redeem Reward Points", fontWeight = FontWeight.Bold) },
                        text = {
                            Text("Are you sure you want to redeem ${String.format("%,d", state.pointsRequired)} points for \"${state.title}\"?")
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.redeemReward(state.title, state.pointsRequired) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Redeem Now", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                is DialogState.BookDining -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text("Resy Table Booking", fontWeight = FontWeight.Bold) },
                        text = {
                            Text("Confirm reservation at ${state.restaurant.name} for the ${state.slot} slot? Exclusive cardholder perk: ${state.restaurant.exclusiveOffer} is automatically linked.")
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.bookDining(state.restaurant, state.slot) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Confirm Booking", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                is DialogState.LoungeQr -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = AmexGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Centurion Lounge Pass", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = state.lounge.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = AmexNavy,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${state.lounge.airport} • Terminal ${state.lounge.terminal}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Box(
                                    modifier = Modifier
                                        .size(180.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .border(2.dp, AmexGold, RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = "QR Code",
                                        tint = AmexNavy,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Text(
                                    text = "Scan this QR code at reception to enter. Eligible for ${state.lounge.eligibility}.",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.dismissDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Done", color = Color.White)
                            }
                        }
                    )
                }
                is DialogState.LockConfirm -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        title = { Text(if (state.card.isLocked) "Unlock Card" else "Lock Card", fontWeight = FontWeight.Bold) },
                        text = {
                            Text(
                                if (state.card.isLocked) "Are you sure you want to unlock ${state.card.name}? Normal transaction flow will resume instantly."
                                else "Are you sure you want to freeze ${state.card.name}? All temporary authorizations and card swipes will be blocked."
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.toggleCardLock(state.card.id, !state.card.isLocked) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmexNavy)
                            ) {
                                Text("Confirm", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}
