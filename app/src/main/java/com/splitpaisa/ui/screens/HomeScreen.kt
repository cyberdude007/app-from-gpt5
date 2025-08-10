package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Expense
import com.splitpaisa.model.Person

@Composable
fun HomeScreen(
    onOpenSplit: () -> Unit,
    recent: List<Expense>,
    people: List<Person>
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(Modifier.weight(2f)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Quick actions", style = MaterialTheme.typography.titleLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onOpenSplit) { Text("+  Add Split") }
                        OutlinedButton(onClick = onOpenSplit) { Text("₹  New Expense") }
                        OutlinedButton(onClick = {}) { Text("New Vault") }
                    }
                }
            }
            Card(Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Today", style = MaterialTheme.typography.titleLarge)
                    Text("Aug 2025")
                    Text("Backed up locally")
                    Text("Tip: Long-press a party to settle")
                }
            }
        }

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
}
