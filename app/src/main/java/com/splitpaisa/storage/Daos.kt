package com.splitpaisa.storage

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons ORDER BY name ASC")
    fun getAll(): Flow<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<PersonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: PersonEntity)
}

@Dao
interface PartyDao {
    @Query("SELECT * FROM parties ORDER BY name ASC")
    fun getAll(): Flow<List<PartyEntity>>

    @Query("SELECT * FROM parties WHERE id = :partyId LIMIT 1")
    fun getById(partyId: String): Flow<PartyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: PartyEntity)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE partyId = :partyId ORDER BY dateEpochDay DESC")
    fun byParty(partyId: String): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ExpenseEntity)
}
