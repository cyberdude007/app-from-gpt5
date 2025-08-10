package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Party

@Composable
fun PartyScreen(
    parties: List<Party>,
    onCreateParty: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Groups", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onCreateParty) { Text("+ Create New Party") }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(parties) { p ->
                Card {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(p.name, style = MaterialTheme.typography.titleMedium)
                        Text("${p.memberIds.size} members", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
