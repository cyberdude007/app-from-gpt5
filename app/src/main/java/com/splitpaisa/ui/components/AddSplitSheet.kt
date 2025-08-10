@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Person

@Composable
fun AddSplitSheet(
    open: Boolean,
    members: List<Person>,
    onDismiss: () -> Unit,
    onSave: (amount: Double, participantIds: List<String>) -> Unit
) {
    if (!open) return
    ModalBottomSheet(onDismissRequest = onDismiss) {
        var amount by remember { mutableStateOf("1200") }
        var selected by remember { mutableStateOf(members.take(2).map { it.id }.toSet()) }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Add Split", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = amount, onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch=='.' } }, label = { Text("Amount (₹)") }, modifier = Modifier.fillMaxWidth())
            MemberSearch(
                members = members.map { it.id to it.name },
                selected = selected,
                onToggle = { id -> selected = if (selected.contains(id)) selected - id else selected + id }
            )
            val per = (amount.toDoubleOrNull() ?: 0.0) / (selected.size.takeIf { it>0 } ?: 1)
            ElevatedCard {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Preview")
                    selected.forEach { id ->
                        val p = members.firstOrNull { it.id == id }?.name ?: "Unknown"
                        ListItem(headlineContent = { Text(p) }, trailingContent = { Text("₹" + String.format("%.2f", per)) })
                        Divider()
                    }
                }
            }
            Button(
                onClick = { onSave(amount.toDoubleOrNull() ?: 0.0, selected.toList()) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save split") }
            Spacer(Modifier.height(12.dp))
        }
    }
}
