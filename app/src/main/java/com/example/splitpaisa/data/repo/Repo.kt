
package com.example.splitpaisa.data.repo

import com.example.splitpaisa.data.db.AppDatabase
import com.example.splitpaisa.data.dao.*
import com.example.splitpaisa.data.entity.*
import com.example.splitpaisa.domain.logic.SplitCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(private val db: AppDatabase) {
    private val accounts = db.accountDao()
    private val people = db.personDao()
    private val categories = db.categoryDao()
    private val txns = db.transactionDao()
    private val postings = db.postingDao()
    private val payments = db.paymentDao()

    fun observeAccounts() = accounts.observeActive()
    fun observeRecentTransactions() = txns.observeRecent()

    data class AccountBalance(val account: AccountEntity, val balancePaise: Long)
    fun observeAccountBalances(): Flow<List<AccountBalance>> =
        combine(accounts.observeActive(), postings.observeAccountDeltas()) { accs, deltas ->
            val map = deltas.associate { it.accountId to it.deltaPaise }
            accs.map { a -> AccountBalance(a, a.openingBalance + (map[a.id] ?: 0L)) }
        }

    suspend fun allPeople(): List<PersonEntity> = people.all()
    suspend fun categoryByName(name: String, type: String) = categories.find(name, type)

    private fun creditAccount(accountId: Long, amount: Long) =
        PostingEntity(ledgerType = "ACCOUNT", refId = accountId, direction = "CREDIT", amountPaise = amount)

    private fun debitAccount(accountId: Long, amount: Long) =
        PostingEntity(ledgerType = "ACCOUNT", refId = accountId, direction = "DEBIT", amountPaise = amount)

    private fun debitExpense(categoryId: Long, amount: Long) =
        PostingEntity(ledgerType = "EXPENSE", refId = categoryId, direction = "DEBIT", amountPaise = amount)

    private fun debitReceivable(personId: Long, amount: Long) =
        PostingEntity(ledgerType = "RECEIVABLE", refId = personId, direction = "DEBIT", amountPaise = amount)

    private fun creditReceivable(personId: Long, amount: Long) =
        PostingEntity(ledgerType = "RECEIVABLE", refId = personId, direction = "CREDIT", amountPaise = amount)

    private fun debitPayable(personId: Long, amount: Long) =
        PostingEntity(ledgerType = "PAYABLE", refId = personId, direction = "DEBIT", amountPaise = amount)

    private fun creditPayable(personId: Long, amount: Long) =
        PostingEntity(ledgerType = "PAYABLE", refId = personId, direction = "CREDIT", amountPaise = amount)

    suspend fun addSplitExpenseYouPaid(accountId: Long, categoryId: Long, totalPaise: Long, participantIds: List<Long>, selfIndex: Int): Long {
        val n = participantIds.size
        require(selfIndex in 0 until n)
        val shares = SplitCalculator.equalSharesPaise(totalPaise, n)
        val txnId = txns.insert(TransactionEntity(totalPaise = totalPaise, categoryId = categoryId, payerType = "ME", accountId = accountId))
        txns.insertSplits(participantIds.mapIndexed { idx, pid -> TransactionSplitEntity(transactionId = txnId, personId = pid, amountPaise = shares[idx], isSelf = idx == selfIndex) })
        val list = mutableListOf<PostingEntity>()
        list += creditAccount(accountId, totalPaise).copy(transactionId = txnId)
        list += debitExpense(categoryId, shares[selfIndex]).copy(transactionId = txnId)
        participantIds.forEachIndexed { idx, pid -> if (idx != selfIndex) list += debitReceivable(pid, shares[idx]).copy(transactionId = txnId) }
        postings.insertAll(list)
        return txnId
    }

    suspend fun addSplitExpenseFriendPaid(friendPersonId: Long, categoryId: Long, totalPaise: Long, participantIds: List<Long>, selfIndex: Int): Long {
        val n = participantIds.size
        require(selfIndex in 0 until n)
        val shares = SplitCalculator.equalSharesPaise(totalPaise, n)
        val yourShare = shares[selfIndex]
        val txnId = txns.insert(TransactionEntity(totalPaise = totalPaise, categoryId = categoryId, payerType = "PERSON", payerPersonId = friendPersonId))
        txns.insertSplits(participantIds.mapIndexed { idx, pid -> TransactionSplitEntity(transactionId = txnId, personId = pid, amountPaise = shares[idx], isSelf = idx == selfIndex) })
        val list = listOf(
            debitExpense(categoryId, yourShare).copy(transactionId = txnId),
            creditPayable(friendPersonId, yourShare).copy(transactionId = txnId)
        )
        postings.insertAll(list)
        return txnId
    }

    suspend fun settle(personId: Long, direction: String, amountPaise: Long, accountId: Long?): Long {
        val payId = payments.insert(PaymentEntity(personId = personId, direction = direction, amountPaise = amountPaise, accountId = accountId))
        val list = mutableListOf<PostingEntity>()
        if (direction == "THEY_PAID_ME") {
            requireNotNull(accountId)
            list += debitAccount(accountId!!, amountPaise).copy(paymentId = payId)
            list += creditReceivable(personId, amountPaise).copy(paymentId = payId)
        } else {
            requireNotNull(accountId)
            list += creditAccount(accountId!!, amountPaise).copy(paymentId = payId)
            list += debitPayable(personId, amountPaise).copy(paymentId = payId)
        }
        postings.insertAll(list)
        return payId
    }

    data class PersonWithNet(val person: PersonEntity, val netPaise: Long)

    suspend fun peopleWithNet(): List<PersonWithNet> {
        val recv = postings.receivableNets().associate { it.personId to it.net }
        val pay = postings.payableNets().associate { it.personId to it.net }
        return people.all().map { p ->
            val net = (recv[p.id] ?: 0L) - (pay[p.id] ?: 0L)
            PersonWithNet(p, net)
        }
    }
}
