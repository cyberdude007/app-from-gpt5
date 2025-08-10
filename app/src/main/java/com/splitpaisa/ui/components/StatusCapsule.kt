package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusCapsule(
    streak: Int,
    xp: Int,
    plainMode: Boolean,
    onPlainModeChange: (Boolean) -> Unit
) {
    Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AssistChip(onClick = {}, label = { Text("$streak day streak") })
            AssistChip(onClick = {}, label = { Text("$xp XP") })
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Plain mode", fontWeight = FontWeight.Medium)
                Switch(checked = plainMode, onCheckedChange = onPlainModeChange)
            }
        }
    }
}
