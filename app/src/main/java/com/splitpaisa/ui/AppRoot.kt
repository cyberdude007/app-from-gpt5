@file:OptIn(ExperimentalMaterial3Api::class)

package com.splitpaisa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppRoot() {
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddSplit by remember { mutableStateOf(false) }
    var showCreateParty by remember { mutableStateOf(false) }
    var isAuthEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("SplitPaisa") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSplit = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Split")
            }
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Authentication: ${if (isAuthEnabled) "ON" else "OFF"}")
                Switch(
                    checked = isAuthEnabled,
                    onCheckedChange = { isAuthEnabled = it }
                )
            }
            Button(onClick = { showCreateParty = true }) { Text("Create Party") }
        }
    }

    if (showAddSplit) {
        AddSplitDialog(
            onDismiss = { showAddSplit = false },
            onSubmit = { amount, description, payer, participants ->
                // TODO: wire this up
                showAddSplit = false
            }
        )
    }

    if (showCreateParty) {
        CreatePartyDialog(
            onDismiss = { showCreateParty = false },
            onCreate = { name, members ->
                // TODO: wire this up
                showCreateParty = false
            }
        )
    }
}

/** ------- Minimal stubs to unblock compile ------- */

@Composable
fun AddSplitDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        amount: Double,
        description: String,
        payer: String,
        participants: List<String>
    ) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Split") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(amount.toDoubleOrNull() ?: 0.0, description, "", emptyList())
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CreatePartyDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, members: List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Party") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Party name") }
            )
        },
        confirmButton = {
            TextButton(onClick = { onCreate(name, emptyList()) }) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
