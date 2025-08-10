package com.splitpaisa.ui.screens

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
import com.splitpaisa.model.Party
import com.splitpaisa.model.Person

@Composable
fun PartiesScreen(
    parties: List<Party>,
    people: List<Person>,
    onOpenParty: (Party) -> Unit,
    onCreateParty: () -> Unit
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Parties", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(parties) { party ->
                PartyCard(
                    party = party,
                    people = people,
                    onClick = { onOpenParty(party) }
                )
            }
        }
    }
}

@Composable
private fun PartyCard(party: Party, people: List<Person>, onClick: () -> Unit) {
    val members = party.memberIds.mapNotNull { id -> people.firstOrNull { it.id == id } }.joinToString { it.name }
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(party.name, style = MaterialTheme.typography.titleMedium)
            Text("Members: $members", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
