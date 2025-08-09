
package com.example.splitpaisa.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DbProvider {
    fun create(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "split_paisa.db")
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO accounts(name, openingBalance, archived) VALUES ('HDFC',0,0)")
                    db.execSQL("INSERT INTO accounts(name, openingBalance, archived) VALUES ('ICICI',0,0)")
                    db.execSQL("INSERT INTO accounts(name, openingBalance, archived) VALUES ('CASH',0,0)")
                    db.execSQL("INSERT INTO people(nickname) VALUES ('Aarav')")
                    db.execSQL("INSERT INTO people(nickname) VALUES ('Meera')")
                    db.execSQL("INSERT INTO people(nickname) VALUES ('Kabir')")
                    db.execSQL("INSERT INTO `groups`(name,simplifyDebts) VALUES ('Test Group',0)")
                    db.execSQL("INSERT INTO group_members(groupId,personId) VALUES (1,1)")
                    db.execSQL("INSERT INTO group_members(groupId,personId) VALUES (1,2)")
                    db.execSQL("INSERT INTO group_members(groupId,personId) VALUES (1,3)")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Food','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Travel','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Groceries','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Entertainment','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Utilities','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Rent','EXPENSE')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Income','INCOME')")
                    db.execSQL("INSERT INTO categories(name,type) VALUES ('Transfer','TRANSFER')")
                }
            })
            .fallbackToDestructiveMigration()
            .build()
}
