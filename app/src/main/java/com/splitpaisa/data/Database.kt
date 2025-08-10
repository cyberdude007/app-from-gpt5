package com.splitpaisa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities=[Member::class, Party::class, PartyMember::class, Account::class, TxnEntity::class, Posting::class, CategoryBudget::class],
    version=1,
    exportSchema=false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun accountDao(): AccountDao
    abstract fun txnDao(): TxnDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile private var I: AppDatabase? = null
        fun get(ctx: Context): AppDatabase =
            I ?: synchronized(this) {
                I ?: Room.databaseBuilder(ctx, AppDatabase::class.java, "splitpaisa.db")
                    .fallbackToDestructiveMigration()
                    .build().also { I = it }
            }
    }
}
