package com.splitpaisa.data

import java.time.LocalDate
import kotlin.math.roundToInt

class FakeRepository {
    val people = mutableListOf<Person>()
    val parties = mutableListOf<Party>()
    val expenses = mutableListOf<Expense>()

    init {
        // Seed people
        val names = listOf("Aarav","Meera","Rohit","Isha","Kabir","Nisha","Dev","Anika")
        names.forEach { people += Person(name = it) }

        // Seed a party with 4 members
        val p1 = Party(name = "Weekend Trip", memberIds = people.take(4).map { it.id }.toMutableList())
        parties += p1

        // Seed a couple of expenses
        expenses += Expense(
            partyId = p1.id, amount = 1200.0, description = "Hotel",
            payerId = p1.memberIds.first(), participantIds = p1.memberIds
        )
        expenses += Expense(
            partyId = p1.id, amount = 600.0, description = "Dinner",
            payerId = p1.memberIds[1], participantIds = p1.memberIds
        )
    }

    fun fuzzyPeople(query: String): List<Person> {
        if (query.isBlank()) return people
        val q = query.lowercase()
        return people.sortedBy { levenshtein(it.name.lowercase(), q) }.take(20)
    }

    fun addParty(name: String, memberIds: List<String>): Party {
        val party = Party(name = name, memberIds = memberIds.toMutableList())
        parties += party
        return party
    }

    fun addExpense(
        partyId: String,
        amount: Double,
        description: String,
        payerId: String,
        participantIds: List<String>
    ): Expense {
        val e = Expense(
            partyId = partyId,
            amount = amount,
            description = description,
            payerId = payerId,
            participantIds = participantIds
        )
        expenses += e
        return e
    }

    fun partyExpenses(partyId: String) = expenses.filter { it.partyId == partyId }

    fun partyBalance(partyId: String): Map<String, Double> {
        // Simple equal split balance calculation
        val members = parties.firstOrNull { it.id == partyId }?.memberIds ?: return emptyMap()
        val totals = members.associateWith { 0.0 }.toMutableMap()
        val ex = partyExpenses(partyId)
        for (e in ex) {
            totals[e.payerId] = (totals[e.payerId] ?: 0.0) + e.amount
            val share = e.amount / e.participantIds.size
            e.participantIds.forEach { pid -> totals[pid] = (totals[pid] ?: 0.0) - share }
        }
        return totals
    }
}

/** Levenshtein distance for simple fuzzy search */
fun levenshtein(a: String, b: String): Int {
    val dp = Array(a.length + 1) { IntArray(b.length + 1) }
    for (i in 0..a.length) dp[i][0] = i
    for (j in 0..b.length) dp[0][j] = j
    for (i in 1..a.length) {
        for (j in 1..b.length) {
            val cost = if (a[i - 1] == b[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + cost
            )
        }
    }
    return dp[a.length][b.length]
}
