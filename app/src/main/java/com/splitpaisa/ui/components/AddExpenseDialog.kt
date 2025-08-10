package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    members: List<Person>,
    onDismiss: () -> Unit,
    onConfirm: (amount: Double, description: String, payerId: String, participantIds: List<String>) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var payerId by remember { mutableStateOf(members.firstOrNull()?.id ?: "") }
    var participants by remember { mutableStateOf(members.map { it.id }.toSet()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = amountText, onValueChange = { amountText = it.filter { ch -> ch.isDigit() || ch == '.' } }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth())
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    val payerName = members.firstOrNull { it.id == payerId }?.name ?: "Select payer"
                    OutlinedTextField(readOnly = true, value = payerName, onValueChange = {}, label = { Text("Payer") }, modifier = Modifier.menuAnchor().fillMaxWidth())
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        members.forEach { p ->
                            DropdownMenuItem(text = { Text(p.name) }, onClick = { payerId = p.id; expanded = false })
                        }
                    }
                }
                PeoplePicker(people = members, preselected = participants, onChange = { participants = it })
            }
        },
        confirmButton = {
            TextButton(
                enabled = amountText.toDoubleOrNull() != null && description.isNotBlank() && payerId.isNotBlank() && participants.isNotEmpty(),
                onClick = { onConfirm(amountText.toDouble(), description.trim(), payerId, participants.toList()) }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
