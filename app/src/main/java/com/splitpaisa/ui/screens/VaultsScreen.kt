package com.splitpaisa.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Account

@Composable
fun VaultsScreen(
    accounts: List<Account>,
    onAddAccount: () -> Unit,
    onAccountClick: (Account) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Accounts", style = MaterialTheme.typography.titleLarge)
            FilledTonalButton(onClick = onAddAccount) { Text("Add") }
        }

        if (accounts.isEmpty()) {
            Text("No accounts yet", style = MaterialTheme.typography.bodyMedium)
        } else {
            accounts.forEach { acc ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAccountClick(acc) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(acc.name, style = MaterialTheme.typography.titleMedium)
                        Text("Type: ${acc.type}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
