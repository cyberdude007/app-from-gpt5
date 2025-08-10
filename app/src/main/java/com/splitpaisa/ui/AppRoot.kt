@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.splitpaisa.repo.DefaultRepository
import com.splitpaisa.ui.components.AddSplitSheet
import com.splitpaisa.ui.components.BottomNav
import com.splitpaisa.ui.components.TopBar
import com.splitpaisa.ui.screens.*
import com.splitpaisa.ui.theme.SplitPaisaTheme
import kotlinx.coroutines.launch

sealed class Screen { object Home: Screen(); object Party: Screen(); object Vaults: Screen(); object Stats: Screen(); object More: Screen() }

@Composable
fun AppRoot() {
    SplitPaisaTheme {
        val context = LocalContext.current
        val repo = remember { DefaultRepository.provide(context) }
        val scope = rememberCoroutineScope()

        var active by remember { mutableStateOf<Screen>(Screen.Home) }
        var plain by remember { mutableStateOf(false) }
        var simplify by remember { mutableStateOf(true) }
        var openSplit by remember { mutableStateOf(false) }

        val parties by repo.partiesFlow.collectAsState(initial = emptyList())
        val people by repo.peopleFlow.collectAsState(initial = emptyList())
        val expensesFlow = parties.firstOrNull()?.let { repo.expensesByPartyFlow(it.id) }
        val recent by expensesFlow?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }

        Scaffold(
            topBar = { TopBar(streak = 7, xp = 245, plainMode = plain, onPlainModeChange = { plain = it }) },
            bottomBar = {
                BottomNav(
                    active = when(active){ Screen.Home->"home"; Screen.Party->"party"; Screen.Vaults->"vaults"; Screen.Stats->"stats"; else->"more" },
                    onChange = {
                        active = when(it){ "home"->Screen.Home; "party"->Screen.Party; "vaults"->Screen.Vaults; "stats"->Screen.Stats; else->Screen.More }
                    }
                )
            }
        ) { inner ->
            Column(Modifier.padding(inner).fillMaxSize().padding(12.dp)) {
                when (active) {
                    Screen.Home -> HomeScreen(onOpenSplit = { openSplit = true }, recent = recent, people = people)
                    Screen.Party -> PartyScreen(parties = parties, onCreateParty = { /* TODO */ })
                    Screen.Vaults -> VaultsScreen()
                    Screen.Stats -> StatsScreen()
                    Screen.More -> MoreScreen(simplify = simplify, onSimplifyChange = { simplify = it }, plain = plain, onPlainChange = { plain = it })
                }
            }
        }

        AddSplitSheet(
            open = openSplit,
            members = people,
            onDismiss = { openSplit = false },
            onSave = { amount, ids ->
                val payer = ids.firstOrNull() ?: people.firstOrNull()?.id ?: return@AddSplitSheet
                scope.launch {
                    val party = parties.firstOrNull() ?: return@launch
                    repo.addExpense(
                        partyId = party.id,
                        amount = amount,
                        description = "Split",
                        payerId = payer,
                        participantIds = ids
                    )
                    openSplit = false
                }
            }
        )
    }
}
