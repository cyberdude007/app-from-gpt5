
package com.example.splitpaisa.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun AddExpenseScreen(nav: NavController, vm: AddExpenseVm = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    var amount by remember { mutableStateOf("500.00") }
    var label by remember { mutableStateOf("Dinner") }
    var payerMe by remember { mutableStateOf(true) }
    var selectedFriend by remember { mutableStateOf(0) }
    val ui by vm.ui.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Add Split Expense", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Total amount (₹)") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
        Spacer(Modifier.height(8.dp))
        Row {
            FilterChip(selected = payerMe, onClick = { payerMe = true }, label = { Text("You paid") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = !payerMe, onClick = { payerMe = false }, label = { Text("Friend paid") })
        }
        if (!payerMe) {
            Spacer(Modifier.height(8.dp))
            Text("Select friend who paid:")
            LazyColumn {
                itemsIndexed(ui.people) { idx, p ->
                    Row { RadioButton(selected = selectedFriend == idx, onClick = { selectedFriend = idx }); Text(p.nickname) }
                }
            }
        } else {
            Spacer(Modifier.height(8.dp))
            Text("Payer account: (defaults to first account for demo)")
        }
        Spacer(Modifier.height(8.dp))
        Text("Participants: for MVP, uses all seeded people (Aarav, Meera, Kabir) with equal split.")
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            scope.launch {
                val paise = (((amount.toDoubleOrNull() ?: 0.0) * 100.0).toLong())
                if (payerMe) vm.saveYouPaid(paise) else vm.saveFriendPaid(paise, selectedFriend)
                nav.popBackStack()
            }
        }) { Text("Save") }
    }
}
