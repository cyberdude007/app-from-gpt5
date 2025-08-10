@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    streak: Int,
    xp: Int,
    plainMode: Boolean,
    onPlainModeChange: (Boolean) -> Unit
) {
    val sw = LocalConfiguration.current.screenWidthDp
    val isNarrow = sw < 400

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(color = Color(0xFF2BB39A), shape = MaterialTheme.shapes.large, modifier = Modifier.size(28.dp)) {}
                Text("SplitPaisa", fontWeight = FontWeight.SemiBold)
                if (!isNarrow) {
                    AssistChip(onClick = {}, label = { Text("INR • Local") })
                }
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (!plainMode && !isNarrow) {
                    Text("$streak day streak", fontSize = 13.sp)
                    Text("•", color = Color.Gray)
                    Text("$xp XP", fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isNarrow) Text("Plain mode", fontSize = 13.sp, modifier = Modifier.padding(end = 6.dp))
                    Switch(checked = plainMode, onCheckedChange = onPlainModeChange)
                }
            }
        }
    )
}
