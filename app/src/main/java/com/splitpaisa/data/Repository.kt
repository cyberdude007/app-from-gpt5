package com.splitpaisa.data

import com.splitpaisa.storage.*
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun parties(): Flow<List<Party>>
    suspend fun createParty(name: String, members: List<String>): Long

    fun accounts(): Flow<List<Account>>
    suspend fun addAccount(name: String, type: String, opening: Double): Long

    fun categories(): Flow<List<Category>>
    suspend fun addCategory(name: String, icon: String, color: Long): Long

    fun recentTransactions(limit: Int = 20): Flow<List<Transaction>>
    suspend fun addTransaction(accountId: Long, partyId: Long?, categoryId: Long?, amount: Double, note: String): Long
    suspend fun deleteTransaction(tx: Transaction)
}

class DefaultRepository(private val db: AppDatabase) : Repository {
    override fun parties() = db.partyDao().getParties()
    override suspend fun createParty(name: String, members: List<String>) =
        db.partyDao().insertPartyWithMembers(name, members)

    override fun accounts() = db.accountDao().getAccounts()
    override suspend fun addAccount(name: String, type: String, opening: Double) =
        db.accountDao().insert(Account(name = name, type = type, openingBalance = opening))

    override fun categories() = db.categoryDao().getCategories()
    override suspend fun addCategory(name: String, icon: String, color: Long) =
        db.categoryDao().insert(Category(name = name, icon = icon, color = color))

    override fun recentTransactions(limit: Int) = db.transactionDao().getRecent(limit)
    override suspend fun addTransaction(accountId: Long, partyId: Long?, categoryId: Long?, amount: Double, note: String) =
        db.transactionDao().insert(Transaction(accountId = accountId, partyId = partyId, categoryId = categoryId, amount = amount, note = note))

    override suspend fun deleteTransaction(tx: Transaction) = db.transactionDao().delete(tx)
}
