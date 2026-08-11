package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CardEntity
import com.example.data.TransactionEntity
import com.example.ui.components.BentoPill
import com.example.ui.theme.AmexAccentGreen
import com.example.ui.theme.AmexCardBlue
import com.example.ui.theme.AmexGold
import com.example.ui.theme.AmexNavy

@Composable
fun FinancialHealthScreen(
    cards: List<CardEntity>,
    transactions: List<TransactionEntity>
) {
    var selectedPeriod by remember { mutableStateOf("This Month") }
    val periods = listOf("This Month", "Last Month", "Last Year")

    val totalCredit = cards.sumOf { it.availableCredit }
    val totalBalance = cards.sumOf { it.balance }
    val creditUtilization = if (totalCredit > 0) ((totalBalance / totalCredit) * 100).toInt() else 18

    val monthlySpend = when (selectedPeriod) {
        "This Month" -> 6842.20
        "Last Month" -> 6120.00
        else -> 5890.00
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. HERO BENTO CARD: FINANCIAL HEALTH OVERVIEW ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = AmexNavy,
            border = androidx.compose.foundation.BorderStroke(1.dp, AmexGold.copy(alpha = 0.3f)),
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FINANCIAL HEALTH & CREDIT SCORE",
                        color = AmexGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    BentoPill(
                        text = "EXCELLENT • 780",
                        backgroundColor = AmexAccentGreen.copy(alpha = 0.2f),
                        textColor = AmexAccentGreen
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Credit Utilisation", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        Text("$creditUtilization%", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = AmexAccentGreen)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Monthly Spending", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        Text("$${String.format("%,.0f", monthlySpend)}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (creditUtilization / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = AmexAccentGreen,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Average Monthly Spend: $6,120", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    Text("Available Credit: $${String.format("%,.0f", totalCredit)}", fontSize = 11.sp, color = AmexGold, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- 2. BENTO SPENDING ANALYTICS BY CATEGORY ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            ),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SPENDING BREAKDOWN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        periods.forEach { period ->
                            val isSelected = selectedPeriod == period
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedPeriod = period },
                                color = if (isSelected) AmexNavy else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = period,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val categoryBreakdowns = listOf(
                    Triple("Travel", 2450.00, Color(0xFF006FCF)),
                    Triple("Dining", 1820.40, Color(0xFFC5A059)),
                    Triple("Shopping", 1120.00, Color(0xFF10B981)),
                    Triple("Groceries", 640.30, Color(0xFF8B5CF6)),
                    Triple("Bills", 480.00, Color(0xFFF59E0B)),
                    Triple("Entertainment", 331.50, Color(0xFFEC4899))
                )

                categoryBreakdowns.forEach { (cat, amount, color) ->
                    val percentage = (amount / monthlySpend).coerceIn(0.0, 1.0).toFloat()

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(cat, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("$${String.format("%,.2f", amount)} (${(percentage * 100).toInt()}%)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { percentage },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = color,
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
