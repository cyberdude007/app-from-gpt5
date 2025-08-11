package com.splitpaisa.data

import com.splitpaisa.storage.Category

// Compile-safe extensions in case Repository doesn't declare these yet.
// Replace with real DAO-backed implementations when ready.
suspend fun Repository.addCategory(name: String, icon: String, color: Long) { /* TODO implement */ }
suspend fun Repository.updateCategory(category: Category) { /* TODO implement */ }
suspend fun Repository.deleteCategory(category: Category) { /* TODO implement */ }
