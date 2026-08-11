package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cardType: String, // "PERSONAL" or "BUSINESS"
    val cardNumberMasked: String,
    val fullNumber: String,
    val cvv: String,
    val expDate: String,
    val balance: Double,
    val availableCredit: Double,
    val paymentDue: Double,
    val dueDate: String,
    val rewardsPoints: Int,
    val rewardsUnit: String, // "Points", "Miles", "Cash Back"
    val isLocked: Boolean = false,
    val colorHex: String,
    val perks: String
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val merchant: String,
    val category: String, // "Dining", "Travel", "Shopping", "Groceries", "Entertainment", "Bills", "Other"
    val amount: Double,
    val date: String,
    val pointsEarned: Int,
    val iconName: String,
    val isPending: Boolean = false
)

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey val id: String,
    val merchant: String,
    val title: String,
    val description: String,
    val category: String,
    val isSaved: Boolean = false,
    val expiryDate: String
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val amount: Double,
    val paymentMethod: String,
    val date: String,
    val status: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class LoungeInfo(
    val id: String,
    val name: String,
    val airport: String,
    val terminal: String,
    val hours: String,
    val amenities: List<String>,
    val eligibility: String
)

data class DiningPlace(
    val id: String,
    val name: String,
    val cuisine: String,
    val location: String,
    val rating: String,
    val exclusiveOffer: String,
    val availableSlots: List<String>
)

data class TravelTrip(
    val id: String,
    val title: String,
    val route: String,
    val dates: String,
    val flightDetails: String,
    val hotelDetails: String,
    val loungePass: String,
    val transportDetails: String
)

data class BenefitInfo(
    val title: String,
    val summary: String,
    val coverageLimit: String,
    val badge: String,
    val details: String
)
