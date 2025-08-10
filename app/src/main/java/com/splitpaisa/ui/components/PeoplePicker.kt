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
import com.splitpaisa.model.Person

@Composable
fun PeoplePicker(
    people: List<Person>,
    preselected: Set<String> = emptySet(),
    onChange: (Set<String>) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(preselected) }
    val filtered = remember(people, query) { fuzzy(people, query) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Search people") }, modifier = Modifier.fillMaxWidth())
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.heightIn(max = 240.dp)) {
            items(filtered) { person ->
                PersonRow(person = person, checked = selected.contains(person.id), onToggle = {
                    selected = if (selected.contains(person.id)) selected - person.id else selected + person.id
                    onChange(selected)
                })
            }
        }
    }
}

private fun fuzzy(people: List<Person>, q: String): List<Person> {
    if (q.isBlank()) return people
    val t = q.lowercase()
    return people.sortedBy { levenshtein(it.name.lowercase(), t) }
}

@Composable
private fun PersonRow(person: Person, checked: Boolean, onToggle: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() })
        Text(person.name, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Levenshtein distance for simple fuzzy search */
private fun levenshtein(a: String, b: String): Int {
    val dp = Array(a.length + 1) { IntArray(b.length + 1) }
    for (i in 0..a.length) dp[i][0] = i
    for (j in 0..b.length) dp[0][j] = j
    for (i in 1..a.length) {
        for (j in 1..b.length) {
            val cost = if (a[i - 1] == b[j - 1]) 0 else 1
            dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
        }
    }
    return dp[a.length][b.length]
}
