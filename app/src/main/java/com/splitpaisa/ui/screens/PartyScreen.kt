package com.splitpaisa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Party

@Composable
fun PartyScreen(
    parties: List<Party>,
    onCreateParty: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Groups", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onCreateParty) { Text("+ Create New Party") }
        }
        parties.forEach {
            Card { ListItem(headlineContent = { Text(it.name) }, supportingContent = { Text("Created just now") }) }
        }
    }
}
