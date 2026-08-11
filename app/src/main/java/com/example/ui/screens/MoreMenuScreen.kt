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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MoreSubTab
import com.example.ui.components.BentoPill
import com.example.ui.theme.AmexGold
import com.example.ui.theme.AmexNavy

data class MenuItemData(
    val subTab: MoreSubTab,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badge: String? = null,
    val badgeColor: Color = AmexGold
)

@Composable
fun MoreMenuScreen(
    onSubTabSelected: (MoreSubTab) -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            subTab = MoreSubTab.ASSISTANT,
            title = "Amex Intelligence",
            description = "Ask our AI copilot anything about your financial habits",
            icon = Icons.Default.AutoAwesome,
            badge = "AI POWERED",
            badgeColor = AmexGold
        ),
        MenuItemData(
            subTab = MoreSubTab.PAYMENTS,
            title = "Payments & Transfers",
            description = "Pay statements, manage Autopay, view receipts",
            icon = Icons.Default.Payment
        ),
        MenuItemData(
            subTab = MoreSubTab.OFFERS,
            title = "Offers & Rebates",
            description = "Save money at restaurants, merchants, and flights",
            icon = Icons.Default.LocalOffer,
            badge = "SAVINGS"
        ),
        MenuItemData(
            subTab = MoreSubTab.DINING,
            title = "Resy Dining",
            description = "Premium Chef's Table and priority reservations",
            icon = Icons.Default.Restaurant
        ),
        MenuItemData(
            subTab = MoreSubTab.FINANCIAL_HEALTH,
            title = "Financial Health",
            description = "Track your credit utilization and category breakdown",
            icon = Icons.Default.TrendingUp
        ),
        MenuItemData(
            subTab = MoreSubTab.BENEFITS,
            title = "Protection & Insurance",
            description = "Active card policy coverage and travel protection",
            icon = Icons.Default.Shield
        ),
        MenuItemData(
            subTab = MoreSubTab.SECURITY,
            title = "Security & Privacy",
            description = "Biometrics, real-time alerts, wallet synching",
            icon = Icons.Default.Security
        ),
        MenuItemData(
            subTab = MoreSubTab.SEARCH,
            title = "Universal Search",
            description = "Instantly search across transactions and benefits",
            icon = Icons.Default.Search
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- HERO BANNER ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = AmexNavy,
            border = androidx.compose.foundation.BorderStroke(1.dp, AmexGold.copy(alpha = 0.3f)),
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "MEMBER SERVICES & PREFERENCES",
                    color = AmexGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "More Premium Services",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Explore exclusive tools, AI financial planning, and lifestyle security controls designed for you.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // --- MENU ITEMS ---
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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SUITE OF CAPABILITIES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                menuItems.forEach { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .clickable { onSubTabSelected(item.subTab) },
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AmexNavy.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = AmexNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (item.badge != null) {
                                        BentoPill(
                                            text = item.badge,
                                            backgroundColor = item.badgeColor.copy(alpha = 0.2f),
                                            textColor = if (item.badgeColor == AmexGold) Color(0xFF856404) else item.badgeColor
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
