package com.splitpaisa.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Party

@Composable
fun PartiesScreen(
    repo: FakeRepository,
    onOpenParty: (Party) -> Unit,
    onCreateParty: () -> Unit
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Parties", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(repo.parties) { party ->
                PartyCard(
                    repo = repo,
                    party = party,
                    onClick = { onOpenParty(party) }
                )
            }
        }
    }
}

@Composable
private fun PartyCard(repo: FakeRepository, party: Party, onClick: () -> Unit) {
    val members = party.memberIds.mapNotNull { id -> repo.people.find { it.id == id } }.joinIntoString()
    val total = repo.partyExpenses(party.id).sumOf { it.amount }
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(party.name, style = MaterialTheme.typography.titleMedium)
            Text("Members: $members", style = MaterialTheme.typography.bodyMedium)
            Text("Total spent: ₹${"%.2f".format(total)}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun List<com.splitpaisa.data.Person>.joinIntoString(): String =
    this.joinToString { it.name }
