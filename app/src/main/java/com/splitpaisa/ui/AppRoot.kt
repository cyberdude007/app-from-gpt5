@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi::class
)

package com.splitpaisa.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AppRoot(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact
) {
    val ctx = LocalContext.current
    val app = ctx.applicationContext as Application
    val repo: Repository = remember { ServiceLocator.repository(app) }
    val settings = remember { SettingsStore(app) }
    val scope = rememberCoroutineScope()

    val plain by settings.plainMode.collectAsState(initial = false)
    val simplify by settings.simplify.collectAsState(initial = false)

    val recent by repo.recentTransactions().collectAsState(initial = emptyList())
    val accounts by repo.accounts().collectAsState(initial = emptyList())
    val parties by repo.parties().collectAsState(initial = emptyList())

    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Tab.Home.route

    var showAddTx by remember { mutableStateOf(false) }
    var showAddParty by remember { mutableStateOf(false) }
    var showAddAccount by remember { mutableStateOf(false) }
    var showTxDetail by remember { mutableStateOf(false) }
    var showTxEdit by remember { mutableStateOf(false) }
    var selectedTx by remember { mutableStateOf<Transaction?>(null) }
    var copyPrefillAmount by rememberSaveable { mutableStateOf<Double?>(null) }
    var copyPrefillNote by rememberSaveable { mutableStateOf("") }


    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SplitPaisa") },
                actions = {
                    if (!isCompact) {
                        StatusCapsule(
                            streak = 7, xp = 245,
                            plainMode = plain,
                            onPlainModeChange = { on -> scope.launch { settings.setPlainMode(on) } }
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (isCompact) {
                BottomBar(current = currentRoute) { route ->
                    if (route != currentRoute) nav.navigate(route)
                }
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
                        onPlainModeChange = { on -> scope.launch { settings.setPlainMode(on) } }
                    )
                }
                Divider()
            }

            NavHost(navController = nav, startDestination = Tab.Home.route, modifier = Modifier.fillMaxSize()) {
                composable(Tab.Home.route) {
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
                composable(Tab.Party.route) {
                    PartyScreen(parties = parties, onCreateParty = { showAddParty = true })
                }
                composable(Tab.Vaults.route) {
                    VaultsScreen(accounts = accounts, onAddAccount = { showAddAccount = true })
                }
                composable(Tab.Stats.route) {
                    StatsScreen()
                }
                composable(Tab.More.route) {
                    MoreScreen(
                        simplify = simplify,
                        onSimplifyChange = { on -> scope.launch { settings.setSimplify(on) } },
                        plain = plain,
                        onPlainChange = { on -> scope.launch { settings.setPlainMode(on) } }
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
            onDismiss = {
                showAddTx = false
                copyPrefillAmount = null
                copyPrefillNote = ""
            },
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
                        copyPrefillAmount = null
                        copyPrefillNote = ""
                    }
                }
            },
            initialAmount = copyPrefillAmount,
            initialNote = copyPrefillNote
        )
    }


    if (showTxDetail && selectedTx != null) {
        TransactionDetailSheet(
            tx = selectedTx!!,
            onDismiss = { showTxDetail = false },
            onEdit = { tx -> showTxDetail = false; selectedTx = tx; showTxEdit = true },
            onCopy = { tx ->
                showTxDetail = false
                copyPrefillAmount = tx.amount
                copyPrefillNote = tx.note
                showAddTx = true
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
