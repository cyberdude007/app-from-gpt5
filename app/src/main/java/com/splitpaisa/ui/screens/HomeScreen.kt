package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Expense
import com.splitpaisa.model.Person

@Composable
fun HomeScreen(
    onOpenSplit: () -> Unit,
    recent: List<Expense>,
    people: List<Person>
) {
    val sw = LocalConfiguration.current.screenWidthDp
    val isCompact = sw < 600

    if (isCompact) {
        // PHONE: vertical stack
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionsCard(compact = true, onOpenSplit = onOpenSplit)
            TodayCard()
            RecentCard(recent = recent, people = people)
        }
    } else {
        // TABLET/WIDE: two-column
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(2f)) { QuickActionsCard(compact = false, onOpenSplit = onOpenSplit) }
                Card(Modifier.weight(1f)) { TodayCard() }
            }
            RecentCard(recent = recent, people = people)
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
            Text("Aug 2025")
            Text("Backed up locally")
            Text("Tip: Long-press a party to settle")
        }
    }
}

@Composable
private fun RecentCard(recent: List<Expense>, people: List<Person>) {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Recent activity", style = MaterialTheme.typography.titleLarge)
            recent.take(10).forEach { e ->
                val payer = people.firstOrNull { it.id == e.payerId }?.name ?: "Unknown"
                ListItem(
                    headlineContent = { Text(e.description) },
                    supportingContent = { Text("$payer • split") },
                    trailingContent = { Text("₹${"%.0f".format(e.amount)}") }
                )
                Divider()
            }
        }
    }
}
