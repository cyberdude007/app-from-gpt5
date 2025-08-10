@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.widget.Toast

@Composable
fun AddTransactionSheet(
    onDismiss: () -> Unit,
    onCreate: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var saving by remember { mutableStateOf(false) }

    fun doSave() {
        val value = amount.toDoubleOrNull()
        if (value == null || value <= 0.0) {
            Toast.makeText(ctx, "Enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }
        saving = true
        scope.launch {
            try {
                onCreate(value, note.trim())
                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
                onDismiss()
            } catch (t: Throwable) {
                Toast.makeText(ctx, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            } finally {
                saving = false
            }
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("New expense", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = amount,
                onValueChange = { input -> amount = input.filter { it.isDigit() || it == '.' } },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { if (!saving) doSave() },
                enabled = !saving,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (saving) "Saving..." else "Save") }
        }
    }
}
