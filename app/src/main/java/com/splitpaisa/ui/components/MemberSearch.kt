package com.splitpaisa.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Chip(text: String, onRemove: (() -> Unit)? = null) {
    AssistChip(onClick = { onRemove?.invoke() }, label = { Text(text) })
}

@Composable
fun MemberSearch(
    members: List<Pair<String,String>>,
    selected: Set<String>,
    onToggle: (String) -> Unit
) {
    var q by remember { mutableStateOf("") }
    val ranked = remember(q, members) {
        if (q.isBlank()) members
        else members.map { it to fuzzyScore(q, it.second) }.sortedByDescending { it.second }.map { it.first }
    }
    val pinned = ranked.filter { selected.contains(it.first) }
    val others = ranked.filterNot { selected.contains(it.first) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = q, onValueChange = { q = it }, label = { Text("Search members") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            pinned.forEach { (id, name) -> Chip(text = name) { onToggle(id) } }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
        ) {
            LazyColumn(modifier = Modifier.heightIn(max = 220.dp)) {
                items(others) { (id, name) ->
                    ListItem(
                        headlineContent = { Text(name) },
                        trailingContent = { Text("Add") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider()
                }
            }
        }
    }
}
