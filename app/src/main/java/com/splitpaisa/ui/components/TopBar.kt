@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    plainMode: Boolean,
    onPlainModeChange: (Boolean) -> Unit
) {
    val sw = LocalConfiguration.current.screenWidthDp
    val narrow = sw < 400
    TopAppBar(
        title = { Text("SplitPaisa", fontWeight = FontWeight.SemiBold) },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (!narrow) Text("Plain mode", fontSize = 13.sp)
                Switch(checked = plainMode, onCheckedChange = onPlainModeChange)
            }
        }
    )
}
