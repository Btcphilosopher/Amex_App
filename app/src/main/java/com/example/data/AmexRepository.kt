package com.example.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AmexRepository(context: Context) {
    private val db = AmexDatabase.getDatabase(context)
    private val cardDao = db.cardDao()
    private val transactionDao = db.transactionDao()
    private val offerDao = db.offerDao()
    private val paymentDao = db.paymentDao()
    private val chatDao = db.chatDao()

    val allCards: Flow<List<CardEntity>> = cardDao.getAllCards()
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allOffers: Flow<List<OfferEntity>> = offerDao.getAllOffers()
    val allPayments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()
    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingCards = cardDao.getAllCards().first()
        if (existingCards.isEmpty()) {
            val initialCards = listOf(
                CardEntity(
                    id = "plat_4821",
                    name = "AMEX PLATINUM",
                    cardType = "PERSONAL",
                    cardNumberMasked = "•••• 4821",
                    fullNumber = "3782 821094 48210",
                    cvv = "1004",
                    expDate = "08/29",
                    balance = 1842.20,
                    availableCredit = 24800.0,
                    paymentDue = 1842.20,
                    dueDate = "Aug 18",
                    rewardsPoints = 184420,
                    rewardsUnit = "Points",
                    colorHex = "#2B2E3A",
                    perks = "$200 Hotel Credit, $240 Digital Entertainment, Global Lounge Access, Fine Hotels & Resorts"
                ),
                CardEntity(
                    id = "gold_1092",
                    name = "AMEX GOLD",
                    cardType = "PERSONAL",
                    cardNumberMasked = "•••• 1092",
                    fullNumber = "3782 410982 10921",
                    cvv = "2041",
                    expDate = "04/28",
                    balance = 640.50,
                    availableCredit = 15000.0,
                    paymentDue = 640.50,
                    dueDate = "Aug 22",
                    rewardsPoints = 42100,
                    rewardsUnit = "Points",
                    colorHex = "#C5A059",
                    perks = "4X Restaurants, 4X US Supermarkets, $120 Uber Cash, $120 Dining Credit"
                ),
                CardEntity(
                    id = "blue_9931",
                    name = "BLUE CASH PREFERRED",
                    cardType = "PERSONAL",
                    cardNumberMasked = "•••• 9931",
                    fullNumber = "3782 993182 99310",
                    cvv = "3092",
                    expDate = "12/27",
                    balance = 310.00,
                    availableCredit = 10000.0,
                    paymentDue = 0.0,
                    dueDate = "Sep 01",
                    rewardsPoints = 320,
                    rewardsUnit = "Cash Back",
                    colorHex = "#006FCF",
                    perks = "6% Cash Back on US Supermarkets, 6% Streaming, 3% Transit"
                ),
                CardEntity(
                    id = "biz_5542",
                    name = "BUSINESS PLATINUM",
                    cardType = "BUSINESS",
                    cardNumberMasked = "•••• 5542",
                    fullNumber = "3782 554210 55429",
                    cvv = "8812",
                    expDate = "09/30",
                    balance = 4120.00,
                    availableCredit = 50000.0,
                    paymentDue = 4120.00,
                    dueDate = "Aug 25",
                    rewardsPoints = 290150,
                    rewardsUnit = "Points",
                    colorHex = "#1B2A4A",
                    perks = "1.5X Points on Key Business Categories, $400 Dell Credit, $360 Indeed Credit"
                ),
                CardEntity(
                    id = "delta_8820",
                    name = "DELTA SKYMILES RESERVE",
                    cardType = "PERSONAL",
                    cardNumberMasked = "•••• 8820",
                    fullNumber = "3782 882041 88203",
                    cvv = "4410",
                    expDate = "01/29",
                    balance = 950.00,
                    availableCredit = 18500.0,
                    paymentDue = 950.00,
                    dueDate = "Sep 05",
                    rewardsPoints = 65000,
                    rewardsUnit = "Miles",
                    colorHex = "#4A1525",
                    perks = "Complimentary Delta Sky Club Access, First Checked Bag Free, Companion Certificate"
                )
            )
            cardDao.insertCards(initialCards)
        }

        val existingTxns = transactionDao.getAllTransactions().first()
        if (existingTxns.isEmpty()) {
            val initialTxns = listOf(
                TransactionEntity("tx_1", "plat_4821", "The Wolseley London", "Dining", 210.40, "Today, 1:15 PM", 1052, "restaurant"),
                TransactionEntity("tx_2", "plat_4821", "British Airways NYC-LHR", "Travel", 1280.00, "Yesterday", 6400, "flight"),
                TransactionEntity("tx_3", "plat_4821", "Saks Fifth Avenue", "Shopping", 285.00, "Aug 09", 285, "shopping_bag"),
                TransactionEntity("tx_4", "gold_1092", "Whole Foods Market", "Groceries", 142.30, "Aug 08", 569, "local_grocery_store"),
                TransactionEntity("tx_5", "plat_4821", "Equinox Fitness", "Bills", 290.00, "Aug 05", 290, "receipt"),
                TransactionEntity("tx_6", "plat_4821", "Uber Black", "Travel", 65.50, "Aug 04", 328, "directions_car"),
                TransactionEntity("tx_7", "gold_1092", "Carbone NYC", "Dining", 340.00, "Aug 02", 1700, "restaurant"),
                TransactionEntity("tx_8", "biz_5542", "Dell Technologies", "Bills", 1850.00, "Jul 30", 2775, "computer"),
                TransactionEntity("tx_9", "plat_4821", "Soho House London", "Entertainment", 180.00, "Jul 28", 180, "local_bar")
            )
            transactionDao.insertTransactions(initialTxns)
        }

        val existingOffers = offerDao.getAllOffers().first()
        if (existingOffers.isEmpty()) {
            val initialOffers = listOf(
                OfferEntity("off_1", "Saks Fifth Avenue", "$50 back", "Spend $250 or more at selected luxury retailers", "Shopping", false, "Expires Sep 30"),
                OfferEntity("off_2", "Resy Restaurants", "3X Points", "Selected top restaurants & fine dining via Resy", "Dining", false, "Expires Oct 15"),
                OfferEntity("off_3", "Fine Hotels & Resorts", "20% back", "Selected travel purchases and resort stays", "Travel", false, "Expires Dec 31"),
                OfferEntity("off_4", "Marriott Bonvoy", "$100 Credit", "Spend $500 on luxury Marriott hotel stays", "Travel", false, "Expires Aug 31"),
                OfferEntity("off_5", "Dell Technologies", "10% back", "Statement credit on qualifying business hardware", "Services", false, "Expires Nov 15")
            )
            offerDao.insertOffers(initialOffers)
        }
    }

    suspend fun toggleCardLock(cardId: String, isLocked: Boolean) {
        cardDao.setCardLockState(cardId, isLocked)
    }

    suspend fun saveOffer(offerId: String, isSaved: Boolean) {
        offerDao.setOfferSavedState(offerId, isSaved)
    }

    suspend fun makePayment(cardId: String, amount: Double, paymentMethod: String) {
        val payment = PaymentEntity(
            id = "pay_${System.currentTimeMillis()}",
            cardId = cardId,
            amount = amount,
            paymentMethod = paymentMethod,
            date = "Today",
            status = "Completed"
        )
        paymentDao.insertPayment(payment)

        // Update card balance
        val cards = cardDao.getAllCards().first()
        val card = cards.find { it.id == cardId }
        if (card != null) {
            val newBalance = (card.balance - amount).coerceAtLeast(0.0)
            val newDue = (card.paymentDue - amount).coerceAtLeast(0.0)
            val newAvailable = card.availableCredit + amount
            cardDao.updateCardBalance(cardId, newBalance, newAvailable, newDue)

            // Add transaction log for payment
            val paymentTx = TransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                cardId = cardId,
                merchant = "Payment - Thank You",
                category = "Bills",
                amount = -amount,
                date = "Just now",
                pointsEarned = 0,
                iconName = "check_circle"
            )
            transactionDao.insertTransaction(paymentTx)
        }
    }

    suspend fun addChatMessage(sender: String, content: String) {
        chatDao.insertMessage(ChatMessageEntity(sender = sender, content = content))
    }

    suspend fun clearChat() {
        chatDao.clearChat()
    }

    // Static Lounges Data
    fun getStaticLounges(): List<LoungeInfo> {
        return listOf(
            LoungeInfo(
                id = "lng_1",
                name = "The Centurion Lounge",
                airport = "LHR - London Heathrow",
                terminal = "Terminal 3, Near Gate 12",
                hours = "5:30 AM - 10:00 PM Daily",
                amenities = listOf("Gourmet Dining by Michelin Chef", "Premium Bar", "Shower Suites", "Private Workstations", "Wi-Fi"),
                eligibility = "Platinum & Centurion Card Members"
            ),
            LoungeInfo(
                id = "lng_2",
                name = "The Centurion Lounge",
                airport = "JFK - New York John F. Kennedy",
                terminal = "Terminal 4, 4th Floor",
                hours = "5:00 AM - 10:30 PM Daily",
                amenities = listOf("1850 Speakeasy Bar", "Equinox Wellness Space", "Hot Buffet", "Private Phone Booths"),
                eligibility = "Platinum & Centurion Card Members"
            ),
            LoungeInfo(
                id = "lng_3",
                name = "Delta Sky Club",
                airport = "LHR - London Heathrow",
                terminal = "Terminal 3",
                hours = "6:00 AM - 9:00 PM",
                amenities = listOf("Craft Cocktail Bar", "Shower Suites", "Flight Assistance Desk"),
                eligibility = "Delta SkyMiles Reserve Card Members"
            ),
            LoungeInfo(
                id = "lng_4",
                name = "The Centurion Lounge",
                airport = "LAX - Los Angeles Intl",
                terminal = "Tom Bradley International Terminal",
                hours = "6:00 AM - 11:00 PM",
                amenities = listOf("Spa Treatments", "Exquisite Wine Tastings", "Family Room", "Quiet Area"),
                eligibility = "Platinum & Centurion Card Members"
            )
        )
    }

    // Static Dining Data
    fun getStaticDiningPlaces(): List<DiningPlace> {
        return listOf(
            DiningPlace(
                id = "din_1",
                name = "Carbone New York",
                cuisine = "Italian • Fine Dining",
                location = "Greenwich Village, NYC",
                rating = "4.9 ★",
                exclusiveOffer = "10X Points + Priority Resy Access",
                availableSlots = listOf("7:30 PM", "8:15 PM", "9:00 PM")
            ),
            DiningPlace(
                id = "din_2",
                name = "The Wolseley London",
                cuisine = "European Grand Cafe",
                location = "Mayfair, London",
                rating = "4.8 ★",
                exclusiveOffer = "Complimentary Champagne + 4X Points",
                availableSlots = listOf("12:30 PM", "1:15 PM", "7:00 PM")
            ),
            DiningPlace(
                id = "din_3",
                name = "Nobu Fifty Seven",
                cuisine = "Japanese Fine Dining",
                location = "Midtown West, NYC",
                rating = "4.9 ★",
                exclusiveOffer = "Chef's Omakase Priority Table",
                availableSlots = listOf("6:00 PM", "8:00 PM", "8:30 PM")
            )
        )
    }

    // Static Upcoming Trip Data
    fun getUpcomingTrip(): TravelTrip {
        return TravelTrip(
            id = "trip_101",
            title = "NEW YORK → LONDON",
            route = "JFK → LHR",
            dates = "Sep 14–21, 2026",
            flightDetails = "British Airways BA 178 • First Class • Seat 2A",
            hotelDetails = "The Claridge's London • Deluxe Suite • 7 Nights",
            loungePass = "Centurion Lounge T3 Terminal Fast-Track",
            transportDetails = "Chauffeur Service to Hotel Confirmed"
        )
    }

    // Static Benefits List
    fun getBenefitsList(): List<BenefitInfo> {
        return listOf(
            BenefitInfo("Purchase Protection", "Coverage for damaged or stolen items purchased with your Card", "Up to $10,000 / incident", "Coverage", "Items purchased within 90 days are protected against theft or accidental damage."),
            BenefitInfo("Extended Warranty", "Adds up to 1 additional year to original manufacturer's warranty", "Up to 1 Additional Year", "Warranty", "Applies to eligible warranties of 5 years or less on purchases made with your Card."),
            BenefitInfo("Return Protection", "Reimbursement if merchant refuses a return within 90 days", "Up to $300 / item", "Protection", "If you try to return an eligible item within 90 days of purchase and merchant won't take it back."),
            BenefitInfo("Trip Delay Insurance", "Reimbursement for essential expenses incurred during trip delays", "Up to $500 / trip", "Travel", "Covers meals, lodging, and personal toiletries when delayed by more than 6 hours."),
            BenefitInfo("Cell Phone Protection", "Coverage for damage or theft when paying monthly phone bill with Card", "Up to $800 / claim", "Mobile", "$50 deductible per claim; maximum 2 approved claims per 12-month period.")
        )
    }
}
