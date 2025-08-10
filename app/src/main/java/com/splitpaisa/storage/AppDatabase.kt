package com.splitpaisa.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(
    entities = [PersonEntity::class, PartyEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun partyDao(): PartyDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context.applicationContext).also { INSTANCE = it }
            }

        private fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "splitpaisa.db")
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: RoomDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val persons = listOf("Aarav","Meera","Rohit","Isha","Kabir","Nisha","Dev","Anika")
                                .map { PersonEntity(id = UUID.randomUUID().toString(), name = it) }
                            val pdao = INSTANCE!!.personDao()
                            val partyDao = INSTANCE!!.partyDao()
                            val expenseDao = INSTANCE!!.expenseDao()
                            pdao.upsertAll(persons)

                            val memberIds = persons.take(4).map { it.id }
                            val party = PartyEntity(
                                id = UUID.randomUUID().toString(),
                                name = "Weekend Trip",
                                memberIdsCsv = memberIds.joinToString(",")
                            )
                            partyDao.upsert(party)

                            val hotel = ExpenseEntity(
                                id = UUID.randomUUID().toString(),
                                partyId = party.id,
                                amount = 1200.0,
                                description = "Hotel",
                                payerId = memberIds.first(),
                                participantIdsCsv = memberIds.joinToString(","),
                                dateEpochDay = java.time.LocalDate.now().toEpochDay()
                            )
                            val dinner = ExpenseEntity(
                                id = UUID.randomUUID().toString(),
                                partyId = party.id,
                                amount = 600.0,
                                description = "Dinner",
                                payerId = memberIds[1],
                                participantIdsCsv = memberIds.joinToString(","),
                                dateEpochDay = java.time.LocalDate.now().toEpochDay()
                            )
                            expenseDao.insert(hotel)
                            expenseDao.insert(dinner)
                        }
                    }
                })
                .build()
        }
    }
}
