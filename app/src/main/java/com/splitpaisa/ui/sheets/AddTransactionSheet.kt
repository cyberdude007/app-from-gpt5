@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddTransactionSheet(
    onDismiss: () -> Unit,
    onCreate: (Double, String) -> Unit,
    initialAmount: Double? = null,
    initialNote: String = ""
) {
    var amount by rememberSaveable { mutableStateOf(initialAmount?.toString() ?: "") }
    var note by rememberSaveable { mutableStateOf(initialNote) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("New expense", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = amount,
                onValueChange = { input: String ->
                    amount = input.filter { ch: Char -> ch.isDigit() || ch == '.' }
                },
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val v = amount.toDoubleOrNull()
                    if (v != null && v > 0.0) onCreate(v, note.trim())
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }
        }
    }
}
