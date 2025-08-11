@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Category

private val presetEmojis = listOf("🍔","🍕","🍜","🍻","🛒","🚕","🏠","🎁","🎟️","📦","💊","🎮","📚","🛠️","🧹","💡","🎵","✈️","🏥","🐾")
private val presetColors = listOf(
    0xFFEF5350, 0xFFAB47BC, 0xFF5C6BC0, 0xFF29B6F6, 0xFF26A69A, 0xFF66BB6A, 0xFFFFEE58, 0xFFFFCA28, 0xFFFF7043, 0xFF8D6E63,
    0xFF9E9E9E, 0xFF26C6DA, 0xFF7E57C2, 0xFF42A5F5, 0xFF26A69A, 0xFF9CCC65
)

@Composable
fun CategoryEditSheet(
    editing: Category?,
    onDismiss: () -> Unit,
    onSave: (name: String, icon: String, color: Long) -> Unit
) {
    var name by remember { mutableStateOf(editing?.name ?: "") }
    var icon by remember { mutableStateOf(editing?.icon ?: "🛒") }
    var color by remember { mutableStateOf<Long>(editing?.color ?: 0xFF26A69A) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(if (editing == null) "Add category" else "Edit category", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Icon", style = MaterialTheme.typography.titleSmall)
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp), modifier = Modifier.heightIn(max = 180.dp)) {
                items(presetEmojis) { emoji ->
                    FilterChip(
                        selected = icon == emoji,
                        onClick = { icon = emoji },
                        label = { Text(emoji) }
                    )
                }
            }

            Text("Color", style = MaterialTheme.typography.titleSmall)
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 44.dp), modifier = Modifier.heightIn(max = 120.dp)) {
                items(presetColors) { c ->
                    AssistChip(
                        onClick = { color = c },
                        label = { },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(c),
                            labelColor = contentColorFor(Color(c))
                        )
                    )
                }
            }

            Button(
                onClick = { if (name.isNotBlank()) onSave(name.trim(), icon, color) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }
        }
    }
}
