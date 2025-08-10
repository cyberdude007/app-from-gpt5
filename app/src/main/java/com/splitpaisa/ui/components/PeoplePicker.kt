package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Person

@Composable
fun PeoplePicker(
    repo: FakeRepository,
    preselected: Set<String> = emptySet(),
    onChange: (Set<String>) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(preselected) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search people") },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.heightIn(max = 240.dp)) {
            items(repo.fuzzyPeople(query)) { person ->
                PersonRow(
                    person = person,
                    checked = selected.contains(person.id),
                    onToggle = {
                        selected = if (selected.contains(person.id)) selected - person.id else selected + person.id
                        onChange(selected)
                    }
                )
            }
        }
    }
}

@Composable
private fun PersonRow(person: Person, checked: Boolean, onToggle: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() })
        Text(person.name, style = MaterialTheme.typography.bodyLarge)
    }
}
