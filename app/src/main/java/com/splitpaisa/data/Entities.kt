package com.splitpaisa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

typealias Paise = Long

@Entity(tableName = "members")
data class Member(@PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String)

@Entity(tableName = "parties")
data class Party(@PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String)

@Entity(
    tableName = "party_members",
    primaryKeys = ["partyId","memberId"],
    indices=[Index("partyId"),Index("memberId")]
)
data class PartyMember(val partyId: Int, val memberId: Int)

@Entity(tableName = "accounts")
data class Account(@PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String, val balancePaise: Paise = 0)

enum class RefType { ACCOUNT, MEMBER }

@Entity(tableName="txns")
data class TxnEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val partyId: Int? = null,
    val dateEpochDays: Int,
    val description: String,
    val category: String,
    val totalPaise: Paise
)

@Entity(
    tableName = "postings",
    foreignKeys=[ForeignKey(entity=TxnEntity::class, parentColumns=["id"], childColumns=["txnId"], onDelete=ForeignKey.CASCADE)],
    indices=[Index("txnId")]
)
data class Posting(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val txnId: Int,
    val refType: RefType,
    val refId: Int,
    val isDebit: Boolean,
    val amountPaise: Paise
)

@Entity(tableName="category_budget")
data class CategoryBudget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val year: Int, val month: Int, val category: String, val budgetPaise: Paise
)

@Dao interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name") fun all(): Flow<List<Member>>
    @Insert suspend fun insertAll(vararg m: Member)
    @Query("SELECT COUNT(*) FROM members") suspend fun count(): Int
}

@Dao interface PartyDao {
    @Query("SELECT * FROM parties ORDER BY name") fun all(): Flow<List<Party>>
    @Insert suspend fun insert(p: Party): Long
    @Insert suspend fun addMembers(vararg x: PartyMember)
    @Query("SELECT m.* FROM members m INNER JOIN party_members pm ON pm.memberId=m.id WHERE pm.partyId=:partyId ORDER BY m.name")
    fun membersOf(partyId: Int): Flow<List<Member>>
}

@Dao interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY name") fun all(): Flow<List<Account>>
    @Insert suspend fun insert(a: Account): Long
    @Query("UPDATE accounts SET balancePaise = balancePaise + :delta WHERE id=:id")
    suspend fun bump(id: Int, delta: Paise)
}

data class TxnWithPostings(
    @Embedded val txn: TxnEntity,
    @Relation(parentColumn="id", entityColumn="txnId") val postings: List<Posting>
)

@Dao interface TxnDao {
    @Transaction @Query("SELECT * FROM txns ORDER BY dateEpochDays DESC, id DESC") fun all(): Flow<List<TxnWithPostings>>
    @Insert suspend fun insertTxn(txn: TxnEntity): Long
    @Insert suspend fun insertPostings(vararg p: Posting)
    @Query("DELETE FROM txns WHERE id=:id") suspend fun deleteTxn(id: Int)
}

@Dao interface BudgetDao {
    @Query("SELECT * FROM category_budget WHERE year=:year AND month=:month ORDER BY category")
    fun forMonth(year: Int, month: Int): Flow<List<CategoryBudget>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(b: CategoryBudget): Long
}
