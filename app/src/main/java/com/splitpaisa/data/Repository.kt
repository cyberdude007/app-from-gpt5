package com.splitpaisa.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Repository private constructor(private val db: AppDatabase, private val prefs: SettingsStore) {
    val members = db.memberDao().all()
    val parties = db.partyDao().all()
    val accounts = db.accountDao().all()
    val txns = db.txnDao().all()

    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (db.memberDao().count() == 0) {
            db.memberDao().insertAll(
                Member(name="Meera"), Member(name="Rahul"), Member(name="Ankit"),
                Member(name="Priya"), Member(name="Sana"), Member(name="Arjun"),
                Member(name="Isha"), Member(name="Dev"), Member(name="Neeraj"),
                Member(name="Fatima"), Member(name="Mohit"), Member(name="Zoya")
            )
            val goaId = db.partyDao().insert(Party(name = "Goa Trip")).toInt()
            listOf(1,2,3,4,5).forEach { mid -> db.partyDao().addMembers(PartyMember(goaId, mid)) }
            val flatId = db.partyDao().insert(Party(name = "Flat 302")).toInt()
            listOf(1,3,4).forEach { mid -> db.partyDao().addMembers(PartyMember(flatId, mid)) }
            db.accountDao().insert(Account(name="Cash", balancePaise = 1250_00))
            db.accountDao().insert(Account(name="HDFC", balancePaise = 8450_00))
            db.accountDao().insert(Account(name="SBI", balancePaise = 412_00))
        }
    }

    fun partyMembers(partyId: Int) = db.partyDao().membersOf(partyId)

    suspend fun addSplit(
        partyId: Int?, description: String, totalPaise: Paise, memberIds: List<Int>, category: String, accountId: Int, date: LocalDate = LocalDate.now()
    ) = withContext(Dispatchers.IO) {
        require(memberIds.isNotEmpty()) { "No members" }
        val shares = rupeeRoundedShares(totalPaise, memberIds.size).toMutableList()
        val debSum = shares.sum(); val delta = totalPaise - debSum
        if (delta != 0L) shares[0] = shares[0] + delta

        val txnId = db.txnDao().insertTxn(
            TxnEntity(partyId = partyId, dateEpochDays = date.toEpochDay().toInt(), description = description, category = category, totalPaise = totalPaise)
        ).toInt()
        val postings = mutableListOf<Posting>()
        memberIds.forEachIndexed { idx, mid -> postings += Posting(txnId = txnId, refType = RefType.MEMBER, refId = mid, isDebit = true, amountPaise = shares[idx]) }
        postings += Posting(txnId = txnId, refType = RefType.ACCOUNT, refId = accountId, isDebit = false, amountPaise = totalPaise)
        db.txnDao().insertPostings(*postings.toTypedArray())
        db.accountDao().bump(accountId, -totalPaise)
        updateGamificationOnActivity(date)
    }

    private suspend fun updateGamificationOnActivity(date: LocalDate) {
        val g = prefs.readGamification()
        val last = if (g.lastActiveEpochDays == 0) null else LocalDate.ofEpochDay(g.lastActiveEpochDays.toLong())
        val today = date
        val newG = if (last == null) {
            g.copy(streakDays = 1, lastActiveEpochDays = today.toEpochDay().toInt(), xp = g.xp + 10)
        } else {
            val gap = ChronoUnit.DAYS.between(last, today).toInt()
            when {
                gap <= 0 -> g.copy(lastActiveEpochDays = today.toEpochDay().toInt())
                gap == 1 -> g.copy(streakDays = g.streakDays + 1, lastActiveEpochDays = today.toEpochDay().toInt(), xp = g.xp + 10)
                else -> {
                    val nowMonth = "${'$'}{today.year}-${'$'}{today.monthValue}"
                    if (g.freezeMonth != nowMonth) {
                        prefs.writeFreezeMonth(nowMonth)
                        g.copy(lastActiveEpochDays = today.toEpochDay().toInt(), xp = g.xp + 5)
                    } else {
                        g.copy(streakDays = 1, lastActiveEpochDays = today.toEpochDay().toInt(), xp = g.xp + 5)
                    }
                }
            }
        }
        prefs.writeGamification(newG)
    }

    companion object {
        @Volatile private var I: Repository? = null
        fun get(context: Context): Repository = I ?: synchronized(this) {
            I ?: Repository(AppDatabase.get(context), SettingsStore.get(context)).also { I = it }
        }
    }
}

fun rupeeRoundedShares(totalPaise: Paise, n: Int): List<Paise> {
    require(n > 0)
    val totalR = totalPaise / 100
    val baseR = totalR / n
    val remR = (totalR % n).toInt()
    val baseP = baseR * 100
    val shares = MutableList(n) { baseP }
    for (i in 0 until remR) shares[i] += 100
    val usedPaise = baseP * n + remR * 100L
    val leftoverPaise = totalPaise - usedPaise
    if (leftoverPaise != 0L && shares.isNotEmpty()) shares[0] += leftoverPaise
    return shares
}
