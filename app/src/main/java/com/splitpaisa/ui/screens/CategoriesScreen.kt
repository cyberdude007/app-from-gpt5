package com.splitpaisa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitpaisa.storage.Category
import com.splitpaisa.ui.sheets.CategoryEditSheet

@Composable
fun CategoriesScreen(
    categories: List<Category>,
    onAdd: (name: String, icon: String, color: Long) -> Unit,
    onUpdate: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Category?>(null) }

    Box(Modifier.fillMaxSize()) {
        if (categories.isEmpty()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("No categories yet", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { editing = null; showSheet = true }) { Text("Add category") }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        FilledTonalButton(onClick = { editing = null; showSheet = true }) { Text("Add") }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                items(categories, key = { it.id }) { cat ->
                    CategoryRow(
                        category = cat,
                        onClick = { editing = cat; showSheet = true },
                        onDelete = onDelete
                    )
                    Divider()
                }
            }
        }

        if (showSheet) {
            CategoryEditSheet(
                editing = editing,
                onDismiss = { showSheet = false },
                onSave = { name, icon, color ->
                    if (editing == null) onAdd(name, icon, color)
                    else onUpdate(editing!!.copy(name = name, icon = icon, color = color))
                    showSheet = false
                }
            )
        }
    }
}

@Composable
private fun CategoryRow(
    category: Category,
    onClick: () -> Unit,
    onDelete: (Category) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(12.dp)
                    .background(Color(category.color), shape = MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(12.dp))
            Text(text = "${category.icon}  ${category.name}", style = MaterialTheme.typography.bodyLarge)
        }
        TextButton(onClick = { onDelete(category) }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
            Text("Delete")
        }
    }
}
