
package com.example.splitpaisa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val openingBalance: Long = 0L,
    val archived: Boolean = false
)

@Entity(tableName = "people")
data class PersonEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val nickname: String)

@Entity(tableName = "groups")
data class GroupEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val simplifyDebts: Boolean = false)

@Entity(tableName = "group_members", primaryKeys = ["groupId","personId"])
data class GroupMemberEntity(val groupId: Long, val personId: Long)

@Entity(tableName = "categories")
data class CategoryEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val type: String)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val totalPaise: Long,
    val categoryId: Long,
    val payerType: String, // ME or PERSON
    val payerPersonId: Long? = null,
    val accountId: Long? = null,
    val label: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "transaction_splits", primaryKeys = ["transactionId","personId"])
data class TransactionSplitEntity(val transactionId: Long, val personId: Long, val amountPaise: Long, val isSelf: Boolean)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val direction: String, // THEY_PAID_ME or I_PAID_THEM
    val amountPaise: Long,
    val accountId: Long? = null,
    val method: String = "OTHER",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "postings")
data class PostingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long? = null,
    val paymentId: Long? = null,
    val ledgerType: String, // ACCOUNT, EXPENSE, RECEIVABLE, PAYABLE
    val refId: Long,
    val direction: String,  // DEBIT or CREDIT
    val amountPaise: Long
)
