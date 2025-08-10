package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPartySheet(
    onDismiss: () -> Unit,
    onCreate: (String, List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var member by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<String>() }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Create party", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Party name") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = member, onValueChange = { member = it }, label = { Text("Add member") }, modifier = Modifier.weight(1f))
                Button(onClick = { if (member.isNotBlank()) { members.add(member); member = "" } }) { Text("Add") }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                members.forEach { m -> AssistChip(onClick = {}, label = { Text(m) }) }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { if (name.isNotBlank()) onCreate(name, members.toList()) },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create") }
        }
    }
}
