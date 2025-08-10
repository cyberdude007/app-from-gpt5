@file:OptIn(ExperimentalMaterial3Api::class)

package com.splitpaisa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.data.FakeRepository
import com.splitpaisa.data.Party
import com.splitpaisa.data.Person
import com.splitpaisa.ui.components.AddExpenseDialog
import com.splitpaisa.ui.components.CreatePartyDialog
import com.splitpaisa.ui.screens.PartiesScreen
import com.splitpaisa.ui.screens.PartyDetailScreen

sealed class Screen {
    data object Parties : Screen()
    data class PartyDetail(val party: Party) : Screen()
    data object Stats : Screen()
    data object Settings : Screen()
}

@Composable
fun AppRoot() {
    val repo = remember { FakeRepository() }
    var screen by remember { mutableStateOf<Screen>(Screen.Parties) }
    var showAddExpense by remember { mutableStateOf(false) }
    var showCreateParty by remember { mutableStateOf(false) }
    var isAuthEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SplitPaisa") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (val s = screen) {
                        is Screen.PartyDetail -> showAddExpense = true
                        else -> showCreateParty = true
                    }
                }
            ) { Icon(Icons.Default.Add, contentDescription = "Add") }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Settings row (auth toggle)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Authentication: ${if (isAuthEnabled) "OFF".takeIf { !isAuthEnabled } ?: "ON" else "OFF"}")
                Switch(checked = isAuthEnabled, onCheckedChange = { isAuthEnabled = it })
            }

            when (val s = screen) {
                Screen.Parties -> PartiesScreen(
                    repo = repo,
                    onOpenParty = { screen = Screen.PartyDetail(it) },
                    onCreateParty = { showCreateParty = true }
                )
                is Screen.PartyDetail -> PartyDetailScreen(
                    repo = repo,
                    party = s.party,
                    onBack = { screen = Screen.Parties },
                    onAddExpense = { showAddExpense = true }
                )
                Screen.Stats -> Text("Stats (coming soon)")
                Screen.Settings -> Text("Settings (coming soon)")
            }
        }
    }

    // Dialogs
    if (showCreateParty) {
        CreatePartyDialog(
            repo = repo,
            onDismiss = { showCreateParty = false },
            onCreated = { party ->
                showCreateParty = false
                // Navigate to new party
                // Update screen here
            }
        )
    }

    if (showAddExpense) {
        val currentParty = (screen as? Screen.PartyDetail)?.party
        if (currentParty != null) {
            AddExpenseDialog(
                repo = repo,
                party = currentParty,
                onDismiss = { showAddExpense = false },
                onAdded = { showAddExpense = false }
            )
        } else {
            showAddExpense = false
        }
    }
}
