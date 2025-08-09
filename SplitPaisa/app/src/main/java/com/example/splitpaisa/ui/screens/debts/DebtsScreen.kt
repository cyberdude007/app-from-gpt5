
package com.example.splitpaisa.ui.screens.debts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.splitpaisa.domain.logic.DebtSimplifier
import com.example.splitpaisa.util.Fmt

@Composable
fun DebtsScreen(nav: NavController, vm: DebtsVm = hiltViewModel()) {
    val nets by vm.nets.collectAsState()
    val settlements = remember(nets) { DebtSimplifier.simplify(nets) }
    Column(Modifier.padding(16.dp)) {
        Text("Simplify Debts (global)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        if (settlements.isEmpty()) Text("No settlements needed.") else settlements.forEach { s ->
            val from = vm.nameFor(s.from); val to = vm.nameFor(s.to)
            Text("$from → $to : ${Fmt.rupees(s.amountPaise)}")
        }
    }
}
