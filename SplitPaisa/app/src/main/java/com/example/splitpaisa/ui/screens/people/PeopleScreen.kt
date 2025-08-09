
package com.example.splitpaisa.ui.screens.people

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.splitpaisa.util.Fmt
import kotlinx.coroutines.launch

@Composable
fun PeopleScreen(nav: NavController, vm: PeopleVm = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val list by vm.people.collectAsState()
    var settleAmount by remember { mutableStateOf("100.00") }
    Column(Modifier.padding(16.dp)) {
        Text("People & Balances", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        list.forEach { it ->
            Row(Modifier.fillMaxWidth()) {
                Text(it.person.nickname, Modifier.weight(1f))
                Text(Fmt.rupees(it.netPaise))
            }
            Row {
                Button(onClick = {
                    scope.launch { vm.settleTheyPaidMe(it.person.id, (((settleAmount.toDoubleOrNull() ?: 0.0) * 100).toLong())) }
                }) { Text("They paid me") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    scope.launch { vm.settleIPaidThem(it.person.id, (((settleAmount.toDoubleOrNull() ?: 0.0) * 100).toLong())) }
                }) { Text("I paid them") }
            }
            Spacer(Modifier.height(8.dp))
        }
        OutlinedTextField(value = settleAmount, onValueChange = { settleAmount = it }, label = { Text("Settle amount (₹)") })
    }
}
