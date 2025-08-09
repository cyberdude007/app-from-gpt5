
package com.example.splitpaisa.domain.logic

object SplitCalculator {
    fun equalSharesPaise(totalPaise: Long, n: Int): List<Long> {
        require(n > 0)
        val base = totalPaise / n
        val rem = (totalPaise % n).toInt()
        return (0 until n).map { i -> base + if (i < rem) 1L else 0L }
    }
}
