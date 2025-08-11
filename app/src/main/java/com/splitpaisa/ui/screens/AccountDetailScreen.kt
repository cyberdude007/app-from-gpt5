package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Account
import com.splitpaisa.storage.Transaction

@Composable
fun AccountDetailScreen(
    account: Account,
    transactions: List<Transaction>,
) {
    val balance = remember(transactions, account.openingBalance) {
        account.openingBalance + transactions.sumOf { it.amount }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(account.name, style = MaterialTheme.typography.titleLarge)
        Text("Type: ${account.type}", style = MaterialTheme.typography.bodyMedium)
        Text("Balance: ₹%.2f".format(balance), style = MaterialTheme.typography.titleMedium)

        Divider()

        if (transactions.isEmpty()) {
            Text("No transactions yet", style = MaterialTheme.typography.bodyMedium)
        } else {
            transactions.forEach { tx ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(tx.note.ifBlank { "Expense" })
                    Text("₹%.2f".format(tx.amount))
                }
                Divider()
            }
        }
    }
}
