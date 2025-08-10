package com.splitpaisa.storage

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PartyDao {
    @Query("SELECT * FROM parties ORDER BY createdAt DESC")
    fun getParties(): Flow<List<Party>>

    @Insert suspend fun insertParty(party: Party): Long
    @Insert suspend fun insertPeople(people: List<Person>)

    @androidx.room.Transaction
    suspend fun insertPartyWithMembers(name: String, members: List<String>): Long {
        val id = insertParty(Party(name = name))
        insertPeople(members.map { Person(partyId = id, name = it) })
        return id
    }
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY createdAt DESC")
    fun getAccounts(): Flow<List<Account>>

    @Insert suspend fun insert(account: Account): Long
    @Update suspend fun update(account: Account)
    @Delete suspend fun delete(account: Account)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getCategories(): Flow<List<Category>>

    @Insert suspend fun insert(cat: Category): Long
    @Update suspend fun update(cat: Category)
    @Delete suspend fun delete(cat: Category)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateUtc DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<Transaction>>

    @Insert suspend fun insert(tx: Transaction): Long
    @Update suspend fun update(tx: Transaction)
    @Delete suspend fun delete(tx: Transaction)

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY dateUtc DESC")
    fun forAccount(accountId: Long): Flow<List<Transaction>>
}
