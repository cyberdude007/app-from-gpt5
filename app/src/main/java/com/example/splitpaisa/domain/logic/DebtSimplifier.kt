
package com.example.splitpaisa.domain.logic

data class Settlement(val from: Long, val to: Long, val amountPaise: Long)

object DebtSimplifier {
    fun simplify(nets: Map<Long, Long>): List<Settlement> {
        val creditors = nets.filter { it.value > 0 }.map { it.key to it.value }.toMutableList()
        val debtors = nets.filter { it.value < 0 }.map { it.key to -it.value }.toMutableList()
        creditors.sortByDescending { it.second }
        debtors.sortByDescending { it.second }
        val result = mutableListOf<Settlement>()
        var i = 0; var j = 0
        while (i < debtors.size && j < creditors.size) {
            var (debtor, d) = debtors[i]
            var (creditor, c) = creditors[j]
            val pay = minOf(d, c)
            if (pay > 0) result += Settlement(from = debtor, to = creditor, amountPaise = pay)
            d -= pay; c -= pay
            if (d == 0L) i++ else debtors[i] = debtor to d
            if (c == 0L) j++ else creditors[j] = creditor to c
        }
        return result
    }
}
