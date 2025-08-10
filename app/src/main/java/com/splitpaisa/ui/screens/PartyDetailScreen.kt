package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Expense
import com.splitpaisa.model.Party
import com.splitpaisa.model.Person

@Composable
fun PartyDetailScreen(
    party: Party,
    people: List<Person>,
    expenses: List<Expense>,
    balance: Map<String, Double>,
    onBack: () -> Unit,
    onAddExpense: () -> Unit
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(party.name, style = MaterialTheme.typography.titleLarge)
        Text("Members: " + party.memberIds.mapNotNull { id -> people.firstOrNull { it.id == id } }.joinToString { it.name })

        Text("Balances:", style = MaterialTheme.typography.titleMedium)
        balance.forEach { (personId, amt) ->
            val p = people.firstOrNull { it.id == personId }?.name ?: "Unknown"
            Text("$p: ${formatRupee(amt)}")
        }

        Spacer(Modifier.height(8.dp))
        Text("Expenses", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(expenses) { e ->
                val payer = people.firstOrNull { it.id == e.payerId }?.name ?: "Unknown"
                Text("• ${e.description}: ${formatRupee(e.amount)} (payer: $payer)")
            }
        }

        Button(onClick = onAddExpense) { Text("Add Expense") }
    }
}

private fun formatRupee(v: Double): String = "₹" + String.format("%.2f", v)
