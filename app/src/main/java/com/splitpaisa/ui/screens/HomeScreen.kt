package com.splitpaisa.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Account
import com.splitpaisa.storage.Transaction

@Composable
fun HomeScreen(
    isCompact: Boolean,
    onOpenSplit: () -> Unit,
    recent: List<Transaction>,
    accounts: List<Account>,
    onTransactionClick: (Transaction) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isCompact) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onOpenSplit, modifier = Modifier.fillMaxWidth()) { Text("₹ New Expense") }
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Add Split") }
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Create Party") }
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Categories") }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onOpenSplit, modifier = Modifier.weight(1f)) { Text("₹ New Expense") }
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Add Split") }
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Create Party") }
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Categories") }
            }
        }

        Card {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Recent", style = MaterialTheme.typography.titleMedium)
                recent.forEach { tx ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onTransactionClick(tx) }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(tx.note.ifBlank { "Expense" })
                        Text("₹%.2f".format(tx.amount))
                    }
                    Divider()
                }
                if (recent.isEmpty()) {
                    Text("No transactions yet", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
