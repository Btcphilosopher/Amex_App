package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BenefitInfo
import com.example.data.CardEntity
import com.example.data.DiningPlace
import com.example.data.LoungeInfo
import com.example.data.OfferEntity
import com.example.data.TransactionEntity
import com.example.ui.theme.AmexGold
import com.example.ui.theme.AmexNavy

@Composable
fun UniversalSearchSheet(
    query: String,
    onQueryChange: (String) -> Unit,
    cards: List<CardEntity>,
    transactions: List<TransactionEntity>,
    offers: List<OfferEntity>,
    lounges: List<LoungeInfo>,
    diningPlaces: List<DiningPlace>,
    benefits: List<BenefitInfo>,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Search Bar Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search transactions, offers, lounges, dining...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (query.isBlank()) {
            Text(
                text = "Type to search across cards, transactions, offers, lounges, dining, and benefits.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            val matchedTxns = transactions.filter { it.merchant.contains(query, true) || it.category.contains(query, true) }
            val matchedOffers = offers.filter { it.merchant.contains(query, true) || it.title.contains(query, true) }
            val matchedLounges = lounges.filter { it.name.contains(query, true) || it.airport.contains(query, true) }
            val matchedDining = diningPlaces.filter { it.name.contains(query, true) || it.cuisine.contains(query, true) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (matchedTxns.isNotEmpty()) {
                    item { Text("Transactions (${matchedTxns.size})", fontWeight = FontWeight.Bold, color = AmexNavy) }
                    items(matchedTxns) { tx ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(tx.merchant, fontWeight = FontWeight.Bold)
                                    Text("${tx.category} • ${tx.date}", fontSize = 11.sp)
                                }
                                Text("$${String.format("%.2f", tx.amount)}", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (matchedOffers.isNotEmpty()) {
                    item { Text("Amex Offers (${matchedOffers.size})", fontWeight = FontWeight.Bold, color = AmexNavy) }
                    items(matchedOffers) { off ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(off.merchant, fontWeight = FontWeight.Bold)
                                Text(off.title, color = AmexGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (matchedLounges.isNotEmpty()) {
                    item { Text("Airport Lounges (${matchedLounges.size})", fontWeight = FontWeight.Bold, color = AmexNavy) }
                    items(matchedLounges) { lng ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(lng.name, fontWeight = FontWeight.Bold)
                                Text(lng.airport, fontSize = 11.sp)
                            }
                        }
                    }
                }

                if (matchedDining.isNotEmpty()) {
                    item { Text("Resy Dining (${matchedDining.size})", fontWeight = FontWeight.Bold, color = AmexNavy) }
                    items(matchedDining) { din ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(din.name, fontWeight = FontWeight.Bold)
                                Text(din.cuisine, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
