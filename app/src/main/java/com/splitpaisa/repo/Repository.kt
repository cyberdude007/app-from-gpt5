package com.splitpaisa.repo

import android.content.Context
import com.splitpaisa.model.*
import com.splitpaisa.storage.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class DefaultRepository private constructor(
    private val db: AppDatabase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch { seedIfEmpty() }
    }

    val peopleFlow: Flow<List<Person>> =
        db.personDao().getAll().map { list -> list.map { it.toModel() } }

    val partiesFlow: Flow<List<Party>> =
        db.partyDao().getAll().map { list -> list.map { it.toModel() } }

    fun expensesByPartyFlow(partyId: String): Flow<List<Expense>> =
        db.expenseDao().byParty(partyId).map { list -> list.map { it.toModel() } }

    suspend fun createParty(name: String, memberIds: List<String>): Party = withContext(Dispatchers.IO) {
        val entity = PartyEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            memberIdsCsv = memberIds.joinToString(",")
        )
        db.partyDao().upsert(entity)
        entity.toModel()
    }

    suspend fun addExpense(
        partyId: String,
        amount: Double,
        description: String,
        payerId: String,
        participantIds: List<String>,
        date: LocalDate = LocalDate.now()
    ) = withContext(Dispatchers.IO) {
        val e = ExpenseEntity(
            id = UUID.randomUUID().toString(),
            partyId = partyId,
            amount = amount,
            description = description,
            payerId = payerId,
            participantIdsCsv = participantIds.joinToString(","),
            dateEpochDay = date.toEpochDay()
        )
        db.expenseDao().insert(e)
    }

    fun balanceFlow(partyId: String): Flow<Map<String, Double>> =
        expensesByPartyFlow(partyId).map { expenses ->
            val totals = mutableMapOf<String, Double>().withDefault { 0.0 }
            expenses.forEach { e ->
                totals[e.payerId] = totals.getValue(e.payerId) + e.amount
                val share = if (e.participantIds.isEmpty()) 0.0 else e.amount / e.participantIds.size
                e.participantIds.forEach { pid ->
                    totals[pid] = totals.getValue(pid) - share
                }
            }
            totals
        }

    private suspend fun seedIfEmpty() {
        if (db.personDao().count() > 0) return
        val persons = listOf("Aarav","Meera","Rohit","Isha","Kabir","Nisha","Dev","Anika")
            .map { PersonEntity(id = UUID.randomUUID().toString(), name = it) }
        db.personDao().upsertAll(persons)

        val memberIds = persons.take(4).map { it.id }
        val party = PartyEntity(
            id = UUID.randomUUID().toString(),
            name = "Weekend Trip",
            memberIdsCsv = memberIds.joinToString(",")
        )
        db.partyDao().upsert(party)

        val hotel = ExpenseEntity(
            id = UUID.randomUUID().toString(),
            partyId = party.id,
            amount = 1200.0,
            description = "Hotel",
            payerId = memberIds.first(),
            participantIdsCsv = memberIds.joinToString(","),
            dateEpochDay = LocalDate.now().toEpochDay()
        )
        val dinner = ExpenseEntity(
            id = UUID.randomUUID().toString(),
            partyId = party.id,
            amount = 600.0,
            description = "Dinner",
            payerId = memberIds[1],
            participantIdsCsv = memberIds.joinToString(","),
            dateEpochDay = LocalDate.now().toEpochDay()
        )
        db.expenseDao().insert(hotel)
        db.expenseDao().insert(dinner)
    }

    companion object {
        @Volatile private var INSTANCE: DefaultRepository? = null
        fun provide(context: Context): DefaultRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DefaultRepository(AppDatabase.get(context)).also { INSTANCE = it }
            }
    }
}

/** --------- Mappers --------- */
private fun PersonEntity.toModel() = Person(id = id, name = name)
private fun PartyEntity.toModel() = Party(
    id = id, name = name,
    memberIds = memberIdsCsv.split(',').filter { it.isNotBlank() }
)
private fun ExpenseEntity.toModel() = Expense(
    id = id, partyId = partyId, amount = amount, description = description, payerId = payerId,
    participantIds = participantIdsCsv.split(',').filter { it.isNotBlank() },
    date = java.time.LocalDate.ofEpochDay(dateEpochDay)
)
