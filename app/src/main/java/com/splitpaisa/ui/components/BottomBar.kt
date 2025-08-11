package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.ui.Tab

@Composable
fun BottomBar(
    current: String,
    onSelect: (String) -> Unit
) {
    Surface(tonalElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Tab.values().forEach { tab ->
                val selected = current == tab.route
                TextButton(onClick = { if (current != tab.route) onSelect(tab.route) }) {
                    Text(
                        text = tab.name,
                        style = if (selected) MaterialTheme.typography.labelLarge
                                else MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
