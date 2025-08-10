package com.splitpaisa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(
    simplify: Boolean,
    onSimplifyChange: (Boolean) -> Unit,
    plain: Boolean,
    onPlainChange: (Boolean) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Preferences", style = MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column { Text("Simplify debts"); Text("Show netted edges between members", style = MaterialTheme.typography.bodySmall) }
                    Switch(checked = simplify, onCheckedChange = onSimplifyChange)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column { Text("Plain mode"); Text("Minimal UI, reduced flourish", style = MaterialTheme.typography.bodySmall) }
                    Switch(checked = plain, onCheckedChange = onPlainChange)
                }
            }
        }
        Card(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Theme", style = MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { Box(Modifier.size(40.dp).background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.large)) }
                }
            }
        }
    }
}
