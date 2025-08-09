
package com.example.splitpaisa.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.splitpaisa.data.dao.*
import com.example.splitpaisa.data.entity.*

@Database(
    entities = [
        AccountEntity::class, PersonEntity::class, GroupEntity::class, GroupMemberEntity::class,
        CategoryEntity::class, TransactionEntity::class, TransactionSplitEntity::class,
        PaymentEntity::class, PostingEntity::class
    ],
    version = 1, exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun personDao(): PersonDao
    abstract fun groupDao(): GroupDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun postingDao(): PostingDao
    abstract fun paymentDao(): PaymentDao
}
