package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Party

@Composable
fun CreatePartyDialog(
    repo: FakeRepository,
    onDismiss: () -> Unit,
    onCreated: (Party) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var members by remember { mutableStateOf(emptySet<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Party") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Party name") }
                )
                Spacer(Modifier.height(12.dp))
                PeoplePicker(
                    repo = repo,
                    onChange = { members = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() && members.size >= 2,
                onClick = {
                    val p = repo.addParty(name.trim(), members.toList())
                    onCreated(p)
                }
            ) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
