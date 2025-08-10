@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.splitpaisa.model.Party
import com.splitpaisa.repo.DefaultRepository
import com.splitpaisa.ui.components.AddExpenseDialog
import com.splitpaisa.ui.components.CreatePartyDialog
import com.splitpaisa.ui.screens.PartiesScreen
import com.splitpaisa.ui.screens.PartyDetailScreen
import kotlinx.coroutines.launch

sealed class Screen {
    data object Parties : Screen()
    data class PartyDetail(val party: Party) : Screen()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppRoot() {
    val context = LocalContext.current
    val repo = remember { DefaultRepository.provide(context) }
    val scope = rememberCoroutineScope()

    val parties by repo.partiesFlow.collectAsState(initial = emptyList())
    val people by repo.peopleFlow.collectAsState(initial = emptyList())

    var screen by remember { mutableStateOf<Screen>(Screen.Parties) }
    var showAddExpense by remember { mutableStateOf(false) }
    var showCreateParty by remember { mutableStateOf(false) }
    var isAuthEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("SplitPaisa") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                when (val s = screen) {
                    is Screen.PartyDetail -> showAddExpense = true
                    else -> showCreateParty = true
                }
            }) { Icon(Icons.Default.Add, contentDescription = "Add") }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Authentication: ${if (isAuthEnabled) "ON" else "OFF"}")
                Switch(checked = isAuthEnabled, onCheckedChange = { isAuthEnabled = it })
            }

            when (val s = screen) {
                Screen.Parties -> PartiesScreen(
                    parties = parties,
                    people = people,
                    onOpenParty = { screen = Screen.PartyDetail(it) },
                    onCreateParty = { showCreateParty = true }
                )
                is Screen.PartyDetail -> {
                    val expenses by repo.expensesByPartyFlow(s.party.id).collectAsState(initial = emptyList())
                    val balance by repo.balanceFlow(s.party.id).collectAsState(initial = emptyMap())
                    PartyDetailScreen(
                        party = s.party,
                        people = people,
                        expenses = expenses,
                        balance = balance,
                        onBack = { screen = Screen.Parties },
                        onAddExpense = { showAddExpense = true }
                    )
                }
            }
        }
    }

    if (showCreateParty) {
        CreatePartyDialog(
            people = people,
            onDismiss = { showCreateParty = false },
            onCreated = { name, members ->
                scope.launch {
                    val party = repo.createParty(name, members.map { it.id })
                    showCreateParty = false
                    screen = Screen.PartyDetail(party)
                }
            }
        )
    }

    if (showAddExpense) {
        val currentParty = (screen as? Screen.PartyDetail)?.party
        if (currentParty != null) {
            val memberPeople = remember(people, currentParty) {
                people.filter { currentParty.memberIds.contains(it.id) }
            }
            AddExpenseDialog(
                members = memberPeople,
                onDismiss = { showAddExpense = false },
                onConfirm = { amount, desc, payerId, participantIds ->
                    scope.launch {
                        repo.addExpense(
                            partyId = currentParty.id,
                            amount = amount,
                            description = desc,
                            payerId = payerId,
                            participantIds = participantIds
                        )
                        showAddExpense = false
                    }
                }
            )
        } else {
            showAddExpense = false
        }
    }
}
