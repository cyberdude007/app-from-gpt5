package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Party

@Composable
fun AddExpenseDialog(
    repo: FakeRepository,
    party: Party,
    onDismiss: () -> Unit,
    onAdded: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var payerId by remember { mutableStateOf(party.memberIds.firstOrNull() ?: "") }
    var participants by remember { mutableStateOf(party.memberIds.toSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Payer dropdown (simple)
                ExposedDropdownMenuBoxSample(
                    repo = repo,
                    memberIds = party.memberIds,
                    selectedId = payerId,
                    onSelected = { payerId = it }
                )
                PeoplePicker(
                    repo = repo,
                    preselected = participants,
                    onChange = { participants = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = amountText.toDoubleOrNull() != null && description.isNotBlank() && payerId.isNotBlank() && participants.isNotEmpty(),
                onClick = {
                    repo.addExpense(
                        partyId = party.id,
                        amount = amountText.toDouble(),
                        description = description.trim(),
                        payerId = payerId,
                        participantIds = participants.toList()
                    )
                    onAdded()
                }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenuBoxSample(
    repo: FakeRepository,
    memberIds: List<String>,
    selectedId: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = memberIds.mapNotNull { id -> repo.people.find { it.id == id } }
    val selectedName = options.firstOrNull { it.id == selectedId }?.name ?: "Select payer"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = selectedName,
            onValueChange = {},
            label = { Text("Payer") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { p ->
                DropdownMenuItem(
                    text = { Text(p.name) },
                    onClick = {
                        onSelected(p.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
