package com.splitpaisa.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Party::class, Person::class, Account::class, Category::class, Transaction::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partyDao(): PartyDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "splitpaisa.db")
                    .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // Pre-populate on first run so "Save" works out of the box.
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        // Default account
                                        db.execSQL(
                                            "INSERT INTO accounts(name,type,openingBalance,createdAt) " +
                                            "VALUES ('Cash','Cash',0.0, strftime('%s','now')*1000)"
                                        )

                                        // Starter categories (teal accent)
                                        val teal = 0xFF2BB39A.toInt() // ARGB teal as signed Int
                                        db.execSQL("INSERT INTO categories(name,icon,color) VALUES ('Food','\uD83C\uDF54', $teal)")
                                        db.execSQL("INSERT INTO categories(name,icon,color) VALUES ('Travel','\u2708\uFE0F', $teal)")
                                        db.execSQL("INSERT INTO categories(name,icon,color) VALUES ('Groceries','\uD83D\uDED2', $teal)")
                                        db.execSQL("INSERT INTO categories(name,icon,color) VALUES ('Utilities','\uD83D\uDCA1', $teal)")
                                        db.execSQL("INSERT INTO categories(name,icon,color) VALUES ('Other','\uD83D\uDCE6', $teal)")
                                    } catch (_: Throwable) {
                                        // best-effort seed
                                    }
                                }
                            }
                        })
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
