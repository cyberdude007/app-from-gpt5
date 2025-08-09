
package com.example.splitpaisa.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.splitpaisa.data.entity.*

@Dao interface AccountDao {
    @Query("SELECT * FROM accounts WHERE archived = 0 ORDER BY name") fun observeActive(): Flow<List<AccountEntity>>
    @Insert suspend fun insert(a: AccountEntity): Long
}

@Dao interface PersonDao {
    @Query("SELECT * FROM people ORDER BY nickname") fun observeAll(): Flow<List<PersonEntity>>
    @Query("SELECT * FROM people ORDER BY nickname") suspend fun all(): List<PersonEntity>
    @Insert suspend fun insert(p: PersonEntity): Long
}

@Dao interface GroupDao {
    @Query("SELECT * FROM `groups` ORDER BY name") fun observeGroups(): Flow<List<GroupEntity>>
    @Insert suspend fun insert(g: GroupEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertMember(m: GroupMemberEntity)
    @Query("SELECT personId FROM group_members WHERE groupId=:groupId") suspend fun memberIds(groupId: Long): List<Long>
}

@Dao interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name") fun observeAll(): Flow<List<CategoryEntity>>
    @Insert suspend fun insert(c: CategoryEntity): Long
    @Query("SELECT * FROM categories WHERE name=:name AND type=:type LIMIT 1") suspend fun find(name: String, type: String): CategoryEntity?
}

@Dao interface TransactionDao {
    @Insert suspend fun insert(t: TransactionEntity): Long
    @Insert suspend fun insertSplits(list: List<TransactionSplitEntity>)
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT 50") fun observeRecent(): Flow<List<TransactionEntity>>
}

data class AccountDelta(val accountId: Long, val deltaPaise: Long)

@Dao interface PostingDao {
    @Insert suspend fun insertAll(list: List<PostingEntity>)
    @Query("SELECT refId AS accountId, SUM(CASE WHEN direction='DEBIT' THEN amountPaise ELSE -amountPaise END) AS deltaPaise FROM postings WHERE ledgerType='ACCOUNT' GROUP BY refId")
    fun observeAccountDeltas(): Flow<List<AccountDelta>>

    @Query("SELECT refId AS personId, SUM(CASE WHEN direction='DEBIT' THEN amountPaise ELSE -amountPaise END) AS net FROM postings WHERE ledgerType='RECEIVABLE' GROUP BY refId")
    suspend fun receivableNets(): List<PersonIdNet>

    @Query("SELECT refId AS personId, SUM(CASE WHEN direction='DEBIT' THEN amountPaise ELSE -amountPaise END) AS net FROM postings WHERE ledgerType='PAYABLE' GROUP BY refId")
    suspend fun payableNets(): List<PersonIdNet>
}

data class PersonIdNet(val personId: Long, val net: Long)

@Dao interface PaymentDao { @Insert suspend fun insert(p: PaymentEntity): Long }
