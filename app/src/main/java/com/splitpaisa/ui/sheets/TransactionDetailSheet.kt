@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.splitpaisa.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Transaction

@Composable
fun TransactionDetailSheet(
    tx: Transaction,
    onDismiss: () -> Unit,
    onEdit: (Transaction) -> Unit,
    onCopy: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Transaction", style = MaterialTheme.typography.titleLarge)
            Text("Amount: ₹%.2f".format(tx.amount))
            Text("Note: ${tx.note}")
            Divider()
            Button(onClick = { onEdit(tx) }, modifier = Modifier.fillMaxWidth()) { Text("Edit") }
            OutlinedButton(onClick = { onCopy(tx) }, modifier = Modifier.fillMaxWidth()) { Text("Copy") }
            TextButton(
                onClick = { onDelete(tx) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) { Text("Delete") }
        }
    }
}
