@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddCategorySheet(
    onDismiss: () -> Unit,
    onCreate: (String, String, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("🍔") }
    var color by remember { mutableStateOf(Color(0xFF2BB39A)) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Add category", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = icon, onValueChange = { icon = it }, label = { Text("Icon (emoji ok)") })
            Button(onClick = { onCreate(name, icon, color.value.toLong()) }, enabled = name.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Create") }
        }
    }
}
