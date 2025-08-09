
package com.example.splitpaisa.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitpaisa.nav.Dest
import com.example.splitpaisa.util.Fmt

@Composable
fun HomeScreen(nav: NavController, vm: HomeVm = hiltViewModel()) {
    val balances by vm.balances.collectAsState(initial = emptyList())
    val recent by vm.recent.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(title = { Text("SplitPaisa") }, actions = {
            TextButton(onClick = { nav.navigate(Dest.Add.route) }) { Text("Add") }
            TextButton(onClick = { nav.navigate(Dest.People.route) }) { Text("People") }
            TextButton(onClick = { nav.navigate(Dest.Accounts.route) }) { Text("Accounts") }
            TextButton(onClick = { nav.navigate(Dest.Debts.route) }) { Text("Debts") }
        })
    }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Accounts", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            balances.forEach { ab ->
                Row(Modifier.fillMaxWidth()) {
                    Text(ab.account.name, Modifier.weight(1f))
                    Text(Fmt.rupees(ab.balancePaise))
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Recent (last 50)", style = MaterialTheme.typography.titleLarge)
            recent.forEach { t -> Text("Txn #${t.id} • ₹${"%.2f".format(t.totalPaise/100.0)} • ${t.payerType}") }
        }
    }
}
