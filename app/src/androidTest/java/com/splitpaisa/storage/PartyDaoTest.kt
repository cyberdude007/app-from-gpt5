package com.splitpaisa.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PartyDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: PartyDao

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.partyDao()
    }

    @After
    fun teardown() { db.close() }

    @Test
    fun insertParty_appearsInFlow() = runBlocking {
        val id = dao.insertParty(Party(name = "Trip"))
        val all = dao.getParties().first()
        assertTrue(all.any { it.id == id && it.name == "Trip" })
    }
}
