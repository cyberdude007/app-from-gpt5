package com.splitpaisa.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNav(
    active: String,
    onChange: (String) -> Unit,
) {
    NavigationBar {
        listOf(
            "home" to "Home",
            "party" to "Party",
            "vaults" to "Vaults",
            "stats" to "Stats",
            "more" to "More"
        ).forEach { (k, label) ->
            NavigationBarItem(
                selected = active == k,
                onClick = { onChange(k) },
                icon = { /* placeholder for icons */ },
                label = { Text(label) }
            )
        }
    }
}
