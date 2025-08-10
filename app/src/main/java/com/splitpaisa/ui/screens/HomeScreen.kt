package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Transaction
import com.splitpaisa.storage.Account

@Composable
fun HomeScreen(
    onOpenSplit: () -> Unit,
    recent: List<Transaction>,
    accounts: List<Account>
) {
    val sw = LocalConfiguration.current.screenWidthDp
    val isCompact = sw < 600

    if (isCompact) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionsCard(compact = true, onOpenSplit = onOpenSplit)
            TodayCard()
            RecentCard(recent = recent)
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(2f)) { QuickActionsCard(compact = false, onOpenSplit = onOpenSplit) }
                Card(Modifier.weight(1f)) { TodayCard() }
            }
            RecentCard(recent = recent)
        }
    }
}

@Composable
private fun QuickActionsCard(compact: Boolean, onOpenSplit: () -> Unit) {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Quick actions", style = MaterialTheme.typography.titleLarge)
            if (compact) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onOpenSplit, modifier = Modifier.fillMaxWidth()) {
                        Text("+ Add Split", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    OutlinedButton(onClick = onOpenSplit, modifier = Modifier.fillMaxWidth()) {
                        Text("\u20B9  New Expense", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                        Text("New Vault", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onOpenSplit) { Text("+  Add Split") }
                    OutlinedButton(onClick = onOpenSplit) { Text("\u20B9  New Expense") }
                    OutlinedButton(onClick = {}) { Text("New Vault") }
                }
            }
        }
    }
}

@Composable
private fun TodayCard() {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Today", style = MaterialTheme.typography.titleLarge)
            Text("Backed up locally")
            Text("Tip: Long-press a party to settle")
        }
    }
}

@Composable
private fun RecentCard(recent: List<Transaction>) {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Recent activity", style = MaterialTheme.typography.titleLarge)
            recent.forEach { e ->
                ListItem(
                    headlineContent = { Text(e.note.ifBlank { "Expense" }) },
                    supportingContent = { Text("₹${"%.0f".format(e.amount)}") },
                    trailingContent = { Text("•") }
                )
                Divider()
            }
        }
    }
}
