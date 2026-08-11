package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CardEntity
import com.example.ui.theme.AmexCardBlue
import com.example.ui.theme.AmexGold
import com.example.ui.theme.AmexNavy

@Composable
fun CardVisual(
    card: CardEntity,
    modifier: Modifier = Modifier,
    onCardClick: (() -> Unit)? = null
) {
    var showFullDetails by remember { mutableStateOf(false) }

    val gradient = when (card.id) {
        "plat_4821" -> Brush.linearGradient(
            colors = listOf(Color(0xFF2C323F), Color(0xFF181C24), Color(0xFF3B4353))
        )
        "gold_1092" -> Brush.linearGradient(
            colors = listOf(Color(0xFFE5C170), Color(0xFFC5A059), Color(0xFF9E7C33))
        )
        "blue_9931" -> Brush.linearGradient(
            colors = listOf(Color(0xFF006FCF), Color(0xFF00448A), Color(0xFF0A192F))
        )
        "biz_5542" -> Brush.linearGradient(
            colors = listOf(Color(0xFF1B2A4A), Color(0xFF0F172A), Color(0xFF00175A))
        )
        "delta_8820" -> Brush.linearGradient(
            colors = listOf(Color(0xFF5A1E2D), Color(0xFF38101C), Color(0xFF7A293E))
        )
        else -> Brush.linearGradient(
            colors = listOf(AmexNavy, AmexCardBlue)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = onCardClick != null) { onCardClick?.invoke() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(gradient)
                .padding(20.dp)
        ) {
            // Lock Overlay if card is locked
            if (card.isLocked) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked Card",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CARD LOCKED",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.matchParentSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AMERICAN EXPRESS",
                            color = if (card.id == "gold_1092") Color(0xFF1E293B) else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = card.name,
                            color = if (card.id == "gold_1092") Color(0xFF2C323F) else AmexGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Contactless & Chip icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp, 24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (card.id == "gold_1092") Color(0xFF1E293B) else Color(0xFFD4AF37)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { showFullDetails = !showFullDetails },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (showFullDetails) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Details",
                                tint = if (card.id == "gold_1092") Color(0xFF1E293B) else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Card Number
                Text(
                    text = if (showFullDetails) card.fullNumber else card.cardNumberMasked,
                    color = if (card.id == "gold_1092") Color(0xFF0F172A) else Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )

                // Footer Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "MEMBER SINCE",
                            color = if (card.id == "gold_1092") Color(0xFF475569) else Color(0xFF94A3B8),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "19",
                            color = if (card.id == "gold_1092") Color(0xFF0F172A) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "EXP",
                            color = if (card.id == "gold_1092") Color(0xFF475569) else Color(0xFF94A3B8),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = card.expDate,
                            color = if (card.id == "gold_1092") Color(0xFF0F172A) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (showFullDetails) {
                        Column {
                            Text(
                                text = "CVV",
                                color = if (card.id == "gold_1092") Color(0xFF475569) else Color(0xFF94A3B8),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = card.cvv,
                                color = if (card.id == "gold_1092") Color(0xFF0F172A) else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Card Type Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (card.id == "gold_1092") Color(0xFF0F172A).copy(alpha = 0.15f)
                                else Color.White.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = card.cardType,
                            color = if (card.id == "gold_1092") Color(0xFF0F172A) else Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
