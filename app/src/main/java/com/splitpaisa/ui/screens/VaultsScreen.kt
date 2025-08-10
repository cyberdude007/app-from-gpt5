package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VaultsScreen() {
    val vaults = listOf("Cash" to 1250, "HDFC" to 8450, "SBI" to 412)
    val total = vaults.sumOf { it.second }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(Modifier.weight(2f)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Accounts", style = MaterialTheme.typography.titleLarge)
                vaults.forEach { (name, bal) ->
                    ListItem(headlineContent = { Text(name) }, trailingContent = { Text("₹$bal") })
                    Divider()
                }
            }
        }
        Card(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Total", style = MaterialTheme.typography.titleLarge)
                Text("₹$total", style = MaterialTheme.typography.displaySmall)
                LinearProgressIndicator(progress = { 0.62f })
            }
        }
    }
}
