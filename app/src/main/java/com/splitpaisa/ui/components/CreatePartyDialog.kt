package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Person

@Composable
fun CreatePartyDialog(
    people: List<Person>,
    onDismiss: () -> Unit,
    onCreated: (name: String, members: List<Person>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var members by remember { mutableStateOf(emptySet<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Party") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Party name") })
                Spacer(Modifier.height(12.dp))
                PeoplePicker(people = people, onChange = { members = it })
            }
        },
        confirmButton = {
            TextButton(enabled = name.isNotBlank() && members.size >= 2, onClick = {
                val selectedPeople = people.filter { members.contains(it.id) }
                onCreated(name.trim(), selectedPeople)
            }) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
