package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

data class GeminiCandidate(
    val content: GeminiContent?
)

interface GeminiRestApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

class GeminiAiService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val api: GeminiRestApi? by lazy {
        try {
            Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(GeminiRestApi::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun queryAmexIntelligence(
        userPrompt: String,
        cards: List<CardEntity>,
        transactions: List<TransactionEntity>,
        offers: List<OfferEntity>
    ): String = withContext(Dispatchers.Default) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasApiKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        // Local Smart Dynamic Engine Analysis
        val diningSpend = transactions.filter { it.category == "Dining" }.sumOf { it.amount }
        val travelSpend = transactions.filter { it.category == "Travel" }.sumOf { it.amount }
        val totalSpend = transactions.sumOf { it.amount }
        val totalPoints = cards.sumOf { it.rewardsPoints }
        val biggestPurchase = transactions.maxByOrNull { it.amount }

        if (hasApiKey && api != null) {
            try {
                val systemPrompt = """
                    You are Amex Intelligence, the high-end AI financial assistant inside the American Express Super App.
                    Be concise, professional, elegant, and helpful.
                    Current Account Summary (Simulated):
                    - Total Membership Rewards Points: $totalPoints
                    - Total Monthly Spending: $$totalSpend
                    - Dining Spend: $$diningSpend
                    - Travel Spend: $$travelSpend
                    - Biggest Purchase: ${biggestPurchase?.merchant} ($${biggestPurchase?.amount})
                    - Active Cards: ${cards.joinToString { "${it.name} (${it.cardNumberMasked})" }}
                    Always state estimates clearly where applicable. Keep responses under 150 words.
                """.trimIndent()

                val req = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = userPrompt)))),
                    systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
                )

                val response = api!!.generateContent(apiKey, req)
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!reply.isNullOrBlank()) {
                    return@withContext reply
                }
            } catch (e: Exception) {
                // Fall back to rule-based response
            }
        }

        // Rule-based fallback handling key questions smoothly
        val lower = userPrompt.lowercase()
        when {
            lower.contains("restaurant") || lower.contains("dining") || lower.contains("food") -> {
                "You have spent **$${String.format("%.2f", diningSpend)}** on restaurants and dining this month across your American Express cards. Your top dining purchase was at **${transactions.firstOrNull { it.category == "Dining" }?.merchant ?: "Carbone NYC"}**."
            }
            lower.contains("point") || lower.contains("rewards") || lower.contains("earned") -> {
                "You currently have **${String.format("%,d", totalPoints)}** Membership Rewards points available. This month you earned **+8,420** points from card purchases and dining multipliers."
            }
            lower.contains("biggest") || lower.contains("largest") || lower.contains("highest") -> {
                if (biggestPurchase != null) {
                    "Your largest purchase this billing cycle was **$${String.format("%.2f", biggestPurchase.amount)}** at **${biggestPurchase.merchant}** (${biggestPurchase.category}) on ${biggestPurchase.date}."
                } else {
                    "Your largest transaction this month was $1,280.00 at British Airways."
                }
            }
            lower.contains("benefit") || lower.contains("perk") || lower.contains("card") -> {
                val platinumCard = cards.find { it.id == "plat_4821" }
                "Your AMEX Platinum card includes premium benefits like the **$200 Hotel Credit**, **$240 Digital Entertainment Credit**, **Global Lounge Collection Access**, **Fine Hotels & Resorts Privileges**, and **$155 Walmart+ Credit**."
            }
            lower.contains("goal") || lower.contains("reach") || lower.contains("spend") -> {
                val gap = 200000 - totalPoints
                if (gap > 0) {
                    "You need **${String.format("%,d", gap)}** more points to reach your next goal of 200,000 Points (valued at a First Class BA ticket to London). Spending approximately $3,890 on 5X Travel category will get you there!"
                } else {
                    "You have surpassed your 180,000 points goal! You now have enough points to redeem a Roundtrip First Class flight to London or 5 nights at Fine Hotels & Resorts."
                }
            }
            else -> {
                "Based on your account overview: Your available credit is **$24,800**, total monthly spend is **$${String.format("%.2f", totalSpend)}**, and you have **${String.format("%,d", totalPoints)} Membership Rewards points**. Let me know if you would like me to analyze transactions, offers, or travel benefits!"
            }
        }
    }
}
