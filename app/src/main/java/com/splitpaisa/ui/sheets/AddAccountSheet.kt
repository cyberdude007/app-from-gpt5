@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddAccountSheet(
    onDismiss: () -> Unit,
    onCreate: (String, String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("CASH") }
    var opening by remember { mutableStateOf("0") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Add account", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Account name") }, modifier = Modifier.fillMaxWidth())
            ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                OutlinedTextField(value = type, onValueChange = {}, readOnly = true, label = { Text("Type") })
            }
            OutlinedTextField(value = opening, onValueChange = { opening = it.filter { ch -> ch.isDigit() or (ch == '.') } }, label = { Text("Opening balance") })
            Button(
                onClick = { onCreate(name, type, opening.toDoubleOrNull() ?: 0.0) },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create") }
        }
    }
}
