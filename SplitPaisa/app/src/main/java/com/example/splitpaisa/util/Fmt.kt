
package com.example.splitpaisa.util

import java.text.NumberFormat
import java.util.Locale

object Fmt {
    private val nf = NumberFormat.getCurrencyInstance(Locale("en","IN")).apply {
        currency = java.util.Currency.getInstance("INR")
        maximumFractionDigits = 2
    }
    fun rupees(paise: Long): String = nf.format(paise / 100.0)
}
