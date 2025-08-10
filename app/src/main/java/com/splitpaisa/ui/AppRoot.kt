package com.splitpaisa.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.splitpaisa.data.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    SplitPaisaTheme {
        val repo = remember { Repository.get(LocalContext.current) }
        val prefs = remember { SettingsStore.get(LocalContext.current) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) { repo.seedIfEmpty() }

        val plain by prefs.plainModeFlow.collectAsState(initial = false)
        val simplify by prefs.simplifyDebtsFlow.collectAsState(initial = true)

        val nav = rememberNavController()
        val backstack by nav.currentBackStackEntryAsState()
        val dest = backstack?.destination?.route ?: "home"

        var showAdd by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SplitPaisa") },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!plain) {
                                var g by remember { mutableStateOf(GamificationState()) }
                                LaunchedEffect(Unit) { g = prefs.readGamification() }
                                Text("🔥 ${'$'}{g.streakDays}")
                                Spacer(Modifier.width(12.dp))
                                Text("⚡ ${'$'}{g.xp}")
                                Spacer(Modifier.width(12.dp))
                            }
                            Text("Plain")
                            Spacer(Modifier.width(6.dp))
                            Switch(checked = plain, onCheckedChange = { scope.launch { prefs.setPlainMode(it) } })
                        }
                    }
                )
            },
            floatingActionButton = {
                if (dest == "home") {
                    FloatingActionButton(onClick = { showAdd = true }) { Icon(Icons.Outlined.Add, null) }
                }
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(selected = dest=="home", onClick={ nav.navigate("home") { popUpTo(0) } }, icon={ Icon(Icons.Outlined.Home,null) }, label={ Text("Home") })
                    NavigationBarItem(selected = dest=="party", onClick={ nav.navigate("party") { popUpTo(0) } }, icon={ Icon(Icons.Outlined.Groups,null) }, label={ Text("Party") })
                    NavigationBarItem(selected = dest=="vaults", onClick={ nav.navigate("vaults") { popUpTo(0) } }, icon={ Icon(Icons.Outlined.Wallet,null) }, label={ Text("Vaults") })
                    NavigationBarItem(selected = dest=="stats", onClick={ nav.navigate("stats") { popUpTo(0) } }, icon={ Icon(Icons.Outlined.BarChart,null) }, label={ Text("Stats") })
                    NavigationBarItem(selected = dest=="more", onClick={ nav.navigate("more") { popUpTo(0) } }, icon={ Icon(Icons.Outlined.MoreHoriz,null) }, label={ Text("More") })
                }
            }
        ) { inner ->
            Box(Modifier.padding(inner)) {
                NavHost(navController = nav, startDestination = "home") {
                    composable("home") { HomeScreen(onOpenAdd = { showAdd = true }) }
                    composable("party") { PartyScreen(simplify = simplify) }
                    composable("vaults") { VaultsScreen() }
                    composable("stats") { StatsScreen() }
                    composable("more") { MoreScreen(
                        simplify = simplify,
                        onSetSimplify = { scope.launch { prefs.setSimplifyDebts(it) } },
                        plain = plain,
                        onSetPlain = { scope.launch { prefs.setPlainMode(it) } }
                    ) }
                }
            }
            if (showAdd) {
                AddSplitDialog(onDismiss = { showAdd = false }) { partyId, desc, totalPaise, members, category, accountId, date ->
                    scope.launch {
                        repo.addSplit(partyId, desc, totalPaise, members, category, accountId, date)
                        showAdd = false
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onOpenAdd: () -> Unit) {
    val repo = Repository.get(LocalContext.current)
    val txns by repo.txns.collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ElevatedCard {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onOpenAdd) { Text("Add Split") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onOpenAdd) { Text("New Expense") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = {}) { Text("New Vault") }
            }
        }
        ElevatedCard {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val now = YearMonth.now()
                Text("Today", style = MaterialTheme.typography.titleMedium)
                Text("${'$'}{now.month} ${'$'}{now.year} • INR • Local")
                Text("Tip: Long-press a party to settle")
            }
        }
        ElevatedCard {
            Column(Modifier.padding(16.dp)) {
                Text("Recent activity", style = MaterialTheme.typography.titleMedium)
                if (txns.isEmpty()) {
                    Text("No activity yet. Add a split to get started.", modifier = Modifier.padding(top=8.dp))
                } else {
                    txns.take(10).forEach { t ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(t.txn.description, fontWeight = FontWeight.SemiBold)
                                Text("${'$'}{t.txn.category} • ${'$'}{LocalDate.ofEpochDay(t.txn.dateEpochDays.toLong())}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("₹" + (t.txn.totalPaise/100.0))
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun PartyScreen(simplify: Boolean) {
    val repo = Repository.get(LocalContext.current)
    val parties by repo.parties.collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Groups", style = MaterialTheme.typography.titleLarge)
            var open by remember { mutableStateOf(false) }
            Button(onClick = { open = true }) { Text("Create New Party") }
            if (open) CreatePartyDialog(onDismiss = { open = false })
        }
        parties.forEach { p ->
            ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                    Text("Simplified debts ${'$'}{if (simplify) "ON" else "OFF"}")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Rahul → Meera • ₹420")
                        TextButton(onClick = {
                            launchUpi(LocalContext.current, "meera@upi", "Meera", 420_00, "Settle: ${'$'}{p.name}")
                        }) { Text("Settle via UPI") }
                    }
                }
            }
        }
    }
}

@Composable
private fun VaultsScreen() {
    val repo = Repository.get(LocalContext.current)
    val accounts by repo.accounts.collectAsState(initial = emptyList())
    val total = accounts.sumOf { it.balancePaise }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ElevatedCard {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Accounts", style = MaterialTheme.typography.titleMedium)
                accounts.forEach {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(it.name)
                        Text("₹" + (it.balancePaise/100.0))
                    }
                    Divider()
                }
            }
        }
        ElevatedCard {
            Column(Modifier.padding(16.dp)) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text("₹" + (total/100.0), style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
private fun StatsScreen() {
    val ym = YearMonth.now()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("This month (${ '$'}{ym.month} ${ '$'}{ym.year})", style = MaterialTheme.typography.titleLarge)
        ElevatedCard { Column(Modifier.padding(16.dp)) { Text("Monthly Trend (line)"); SimpleLine(height = 120) } }
        ElevatedCard { Column(Modifier.padding(16.dp)) { Text("Cash-flow (bars)"); SimpleBars(height = 120) } }
        ElevatedCard { Column(Modifier.padding(16.dp)) { Text("Category sparks"); Row { repeat(4) { Box(Modifier.padding(6.dp)) { SimpleLine(height = 40, width = 120) } } } } }
        ElevatedCard { Column(Modifier.padding(16.dp)) { Text("Daily heatmap"); HeatmapGrid() } }
        ElevatedCard { Column(Modifier.padding(16.dp)) { Text("Budget vs Actual"); SimpleDualBars() } }
    }
}

@Composable
private fun MoreScreen(simplify: Boolean, onSetSimplify: (Boolean)->Unit, plain: Boolean, onSetPlain: (Boolean)->Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ElevatedCard {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Preferences", style = MaterialTheme.typography.titleMedium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text("Simplify debts", fontWeight = FontWeight.SemiBold); Text("Show netted edges", style = MaterialTheme.typography.bodySmall) }
                    Switch(checked = simplify, onCheckedChange = onSetSimplify)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text("Plain mode", fontWeight = FontWeight.SemiBold); Text("Minimal UI", style = MaterialTheme.typography.bodySmall) }
                    Switch(checked = plain, onCheckedChange = onSetPlain)
                }
            }
        }
        ElevatedCard {
            Column(Modifier.padding(16.dp)) {
                Text("Theme", style = MaterialTheme.typography.titleMedium)
                Row(Modifier.padding(top = 8.dp)) {
                    Box(Modifier.size(36.dp).background(Teal))
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(36.dp).background(Lime))
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(36.dp).background(Gold))
                }
            }
        }
    }
}

// ---------- Simple chart placeholders ----------
@Composable private fun SimpleLine(width: Int = 0, height: Int = 80) {
    androidx.compose.foundation.Canvas(modifier = Modifier.width((if (width==0) 260 else width).dp).height(height.dp)) {
        val w = size.width; val h = size.height
        val pts = List(12) { i -> androidx.compose.ui.geometry.Offset(i/11f * w, h * (0.3f + 0.6f * kotlin.random.Random(i).nextFloat())) }
        for (i in 0 until pts.lastIndex) drawLine(Teal, pts[i], pts[i+1], strokeWidth = 4f)
    }
}
@Composable private fun SimpleBars(width: Int = 0, height: Int = 100) {
    androidx.compose.foundation.Canvas(modifier = Modifier.width((if (width==0) 260 else width).dp).height(height.dp)) {
        val w = size.width; val h = size.height; val n = 6; val gap = 8f
        val barW = (w - gap*(n+1)) / n
        for (i in 0 until n) {
            val valH = h * (0.2f + 0.7f * kotlin.random.Random(i).nextFloat())
            drawRect(Lime, topLeft = androidx.compose.ui.geometry.Offset(gap + i*(barW+gap), h - valH), size = androidx.compose.ui.geometry.Size(barW, valH))
        }
    }
}
@Composable private fun SimpleDualBars(width: Int = 0, height: Int = 120) {
    androidx.compose.foundation.Canvas(modifier = Modifier.width((if (width==0) 300 else width).dp).height(height.dp)) {
        val w = size.width; val h = size.height; val n = 4; val gap = 10f
        val barW = (w - gap*(n+1)) / n
        for (i in 0 until n) {
            val b = h * (0.4f + 0.2f * kotlin.random.Random(i).nextFloat())
            val a = b * (0.9f + 0.2f * kotlin.random.Random(i+10).nextFloat())
            val x = gap + i*(barW+gap)
            drawRect(Gold, topLeft = androidx.compose.ui.geometry.Offset(x, h - b), size = androidx.compose.ui.geometry.Size(barW, b))
            drawRect(Teal, topLeft = androidx.compose.ui.geometry.Offset(x+barW*0.15f, h - a), size = androidx.compose.ui.geometry.Size(barW*0.7f, a))
        }
    }
}
@Composable private fun HeatmapGrid(cols: Int = 7, rows: Int = 6) {
    Column {
        repeat(rows) { r ->
            Row {
                repeat(cols) { c ->
                    val level = (r*cols + c) % 5
                    Box(Modifier.size(18.dp).background(
                        when(level){0->Teal.copy(0.1f);1->Teal.copy(0.2f);2->Teal.copy(0.4f);3->Teal.copy(0.6f);else->Teal}
                    ))
                    Spacer(Modifier.width(3.dp))
                }
            }
            Spacer(Modifier.height(3.dp))
        }
    }
}

private fun launchUpi(ctx: Context, vpa: String, name: String, amountPaise: Paise, note: String) {
    val am = "%.2f".format(amountPaise / 100.0)
    val enc = { s: String -> URLEncoder.encode(s, StandardCharsets.UTF_8) }
    val uri = Uri.parse("upi://pay?pa=${enc(vpa)}&pn=${enc(name)}&am=$am&cu=INR&tn=${enc(note)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try { ctx.startActivity(intent) } catch (e: Exception) { Toast.makeText(ctx, "No UPI app found", Toast.LENGTH_SHORT).show() }
}
