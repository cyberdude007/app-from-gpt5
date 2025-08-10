package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Account

@Composable
fun VaultsScreen(
    accounts: List<Account>,
    onAddAccount: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Accounts", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onAddAccount) { Text("+ Add Account") }
        }
        accounts.forEach { a ->
            Card {
                ListItem(
                    headlineContent = { Text(a.name) },
                    trailingContent = { Text("₹${"%.0f".format(a.openingBalance)}") }
                )
            }
        }
    }
}
