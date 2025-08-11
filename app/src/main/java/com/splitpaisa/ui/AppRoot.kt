package com.splitpaisa.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.splitpaisa.data.Repository
import com.splitpaisa.di.ServiceLocator
import com.splitpaisa.settings.SettingsStore
import com.splitpaisa.storage.Account
import com.splitpaisa.storage.Transaction
import com.splitpaisa.ui.components.StatusCapsule
import com.splitpaisa.ui.components.TopBar
import com.splitpaisa.ui.screens.*
import com.splitpaisa.ui.sheets.*
import com.splitpaisa.ui.theme.SplitPaisaTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

enum class Tab { Home, Party, Vaults, Stats, More }

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val repo: Repository = remember { ServiceLocator.repository(context) }
    val settings: SettingsStore = remember { ServiceLocator.settings(context) }
    val scope = rememberCoroutineScope()

    val plain by settings.plainMode.collectAsState(initial = false)
    val simplify by settings.simplify.collectAsState(initial = true)

    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val sel = Tab.values().firstOrNull { it.name == backStack?.destination?.route } ?: Tab.Home

    val recent by repo.recentTransactions().collectAsState(initial = emptyList<Transaction>())
    val accounts by repo.accounts().collectAsState(initial = emptyList<Account>())
    val parties by repo.parties().collectAsState(initial = emptyList())

    var showAddParty by remember { mutableStateOf(false) }
    var showAddAccount by remember { mutableStateOf(false) }
    var showAddTx by remember { mutableStateOf(false) }

    SplitPaisaTheme {
        Scaffold(
            topBar = { TopBar(plainMode = plain, onPlainModeChange = { scope.launch { settings.setPlainMode(it) } }) },
            bottomBar = {
                NavigationBar {
                    Tab.values().forEach {
                        NavigationBarItem(
                            selected = sel == it,
                            onClick = { nav.navigate(it.name) { launchSingleTop = true } },
                            label = { Text(it.name) },
                            icon = { Icon(Icons.Filled.Circle, contentDescription = null) }
                        )
                    }
                }
            }
        ) { pad ->
            Column(Modifier.padding(pad).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusCapsule(streak = 7, xp = 245, plainMode = plain, onPlainModeChange = { scope.launch { settings.setPlainMode(it) } })

                NavHost(navController = nav, startDestination = Tab.Home.name) {
                    composable(Tab.Home.name) {
                        HomeScreen(
                            onOpenSplit = { showAddTx = true },
                            recent = recent,
                            accounts = accounts
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
        AddTransactionSheet(onDismiss = { showAddTx = false }, onCreate = { amt, note ->
            val ctx = LocalContext.current
            val firstAcc = accounts.firstOrNull()
            if (firstAcc == null) {
                Toast.makeText(ctx, "Please create an account first.", Toast.LENGTH_SHORT).show()
                showAddTx = false
                showAddAccount = true
            } else {
                scope.launch {
                    repo.addTransaction(firstAcc.id, null, null, amt, note)
                    showAddTx = false
                }
            }
        })
    }
}
