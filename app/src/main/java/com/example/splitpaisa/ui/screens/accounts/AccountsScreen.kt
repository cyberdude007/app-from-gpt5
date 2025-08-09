
package com.example.splitpaisa.ui.screens.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.splitpaisa.util.Fmt

@Composable
fun AccountsScreen(nav: NavController, vm: AccountsVm = hiltViewModel()) {
    val balances by vm.balances.collectAsState(initial = emptyList())
    Column(Modifier.padding(16.dp)) {
        Text("Accounts", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        balances.forEach {
            Row(Modifier.fillMaxWidth()) {
                Text(it.account.name, Modifier.weight(1f))
                Text(Fmt.rupees(it.balancePaise))
            }
        }
    }
}
