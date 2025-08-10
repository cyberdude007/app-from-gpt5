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
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Party

@Composable
fun PartyDetailScreen(
    repo: FakeRepository,
    party: Party,
    onBack: () -> Unit,
    onAddExpense: () -> Unit
) {
    val ex = repo.partyExpenses(party.id)
    val balance = repo.partyBalance(party.id)

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(party.name, style = MaterialTheme.typography.titleLarge)
        Text("Members: " + party.memberIds.mapNotNull { id -> repo.people.find { it.id == id } }.joinToString { it.name })

        Text("Balances:", style = MaterialTheme.typography.titleMedium)
        balance.forEach { (personId, amt) ->
            val p = repo.people.firstOrNull { it.id == personId }?.name ?: "Unknown"
            Text("$p: ${formatRupee(amt)}")
        }

        Spacer(Modifier.height(8.dp))
        Text("Expenses", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(ex) { e ->
                Text("• ${e.description}: ${formatRupee(e.amount)} (payer: ${repo.people.firstOrNull { it.id == e.payerId }?.name})")
            }
        }

        Button(onClick = onAddExpense) { Text("Add Expense") }
    }
}

private fun formatRupee(v: Double): String =
    "₹" + String.format("%.2f", v)
