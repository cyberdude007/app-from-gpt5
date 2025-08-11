package com.splitpaisa.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.splitpaisa.data.Repository
import com.splitpaisa.di.ServiceLocator
import com.splitpaisa.settings.SettingsStore
import com.splitpaisa.storage.Transaction
import com.splitpaisa.ui.components.BottomBar
import com.splitpaisa.ui.components.StatusCapsule
import com.splitpaisa.ui.screens.*
import com.splitpaisa.ui.sheets.*
import kotlinx.coroutines.launch
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun AppRoot(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact
) {
    val ctx = LocalContext.current
    val app = ctx.applicationContext as Application
    val repo: Repository = remember { ServiceLocator.repository(app) }
    val settings = remember { SettingsStore(app) }
    val scope = rememberCoroutineScope()

    val plain by settings.readPlainMode().collectAsState(initial = false)
    val simplify by settings.readSimplify().collectAsState(initial = false)

    val recent by repo.recentTransactions().collectAsState(initial = emptyList())
    val accounts by repo.accounts().collectAsState(initial = emptyList())
    val parties by repo.parties().collectAsState(initial = emptyList())

    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Tab.Home.name

    var showAddTx by remember { mutableStateOf(false) }
    var showAddParty by remember { mutableStateOf(false) }
    var showAddAccount by remember { mutableStateOf(false) }
    var showTxDetail by remember { mutableStateOf(false) }
    var showTxEdit by remember { mutableStateOf(false) }
    var selectedTx by remember { mutableStateOf<com.splitpaisa.storage.Transaction?>(null) }

    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("SplitPaisa") },
                actions = {
                    if (!isCompact) {
                        StatusCapsule(
                            streak = 7, xp = 245,
                            plainMode = plain,
                            onPlainModeChange = { scope.launch { settings.setPlainMode(it) } }
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (isCompact) {
                BottomBar(current = currentRoute, onSelect = { route -> nav.navigate(route) })
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            if (isCompact) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusCapsule(
                        streak = 7, xp = 245,
                        plainMode = plain,
                        onPlainModeChange = { scope.launch { settings.setPlainMode(it) } }
                    )
                }
                Divider()
            }

            NavHost(navController = nav, startDestination = Tab.Home.name, modifier = Modifier.fillMaxSize()) {
                composable(Tab.Home.name) {
                    HomeScreen(
                        isCompact = isCompact,
                        onOpenSplit = { showAddTx = true },
                        recent = recent,
                        accounts = accounts,
                        onTransactionClick = { tx ->
                            selectedTx = tx
                            showTxDetail = true
                        }
                    )
                }
                composable(Tab.Party.name) {
                    PartyScreen(parties = parties, onCreateParty = { showAddParty = true })
                }
                composable(Tab.Vaults.name) {
                    VaultsScreen(accounts = accounts, onAddAccount = { showAddAccount = true })
                }
                composable(Tab.Stats.name) {
                    StatsScreen()
                }
                composable(Tab.More.name) {
                    MoreScreen(
                        simplify = simplify,
                        onSimplifyChange = { scope.launch { settings.setSimplify(it) } },
                        plain = plain,
                        onPlainChange = { scope.launch { settings.setPlainMode(it) } }
                    )
                }
            }
        }
    }

    if (showAddParty) {
        AddPartySheet(onDismiss = { showAddParty = false }, onCreate = { name, members ->
            scope.launch { repo.createParty(name, members); showAddParty = false }
        })
    }

    if (showAddAccount) {
        AddAccountSheet(onDismiss = { showAddAccount = false }, onCreate = { name, type, opening ->
            scope.launch { repo.addAccount(name, type, opening); showAddAccount = false }
        })
    }

    if (showAddTx) {
        AddTransactionSheet(
            onDismiss = { showAddTx = false },
            onCreate = { amt, note ->
                val maybeAcc = accounts.firstOrNull()?.id
                if (maybeAcc == null) {
                    Toast.makeText(ctx, "Create an account first", Toast.LENGTH_SHORT).show()
                    showAddTx = false
                    showAddAccount = true
                } else {
                    scope.launch {
                        repo.addTransaction(maybeAcc, null, null, amt, note)
                        showAddTx = false
                    }
                }
            }
        )
    }

    if (showTxDetail && selectedTx != null) {
        TransactionDetailSheet(
            tx = selectedTx!!,
            onDismiss = { showTxDetail = false },
            onEdit = { tx -> showTxDetail = false; selectedTx = tx; showTxEdit = true },
            onCopy = { tx ->
                scope.launch {
                    repo.addTransaction(tx.accountId, tx.partyId, tx.categoryId, tx.amount, tx.note)
                    showTxDetail = false
                }
            },
            onDelete = { tx ->
                scope.launch {
                    repo.deleteTransaction(tx)
                    showTxDetail = false
                }
            }
        )
    }

    if (showTxEdit && selectedTx != null) {
        UpdateTransactionSheet(
            tx = selectedTx!!,
            onDismiss = { showTxEdit = false },
            onSave = { updated ->
                scope.launch {
                    repo.updateTransaction(updated)
                    showTxEdit = false
                }
            }
        )
    }
}
