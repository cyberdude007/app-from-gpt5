package com.splitpaisa.data

import java.util.UUID
import java.time.LocalDate

data class Person(
    val id: String = UUID.randomUUID().toString(),
    val name: String
)

data class Party(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val memberIds: MutableList<String> = mutableListOf()
)

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val partyId: String,
    val amount: Double,
    val description: String,
    val payerId: String,
    val participantIds: List<String>,
    val date: LocalDate = LocalDate.now()
)
