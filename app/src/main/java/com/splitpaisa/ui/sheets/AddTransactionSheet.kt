package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTransactionSheet(
    onDismiss: () -> Unit,
    onCreate: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("New expense", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = amount, onValueChange = { amount = it.filter { ch -> ch.isDigit() or (ch == '.') } }, label = { Text("Amount") })
            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { onCreate(amount.toDoubleOrNull() ?: 0.0, note) }, enabled = amount.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Save") }
        }
    }
}
