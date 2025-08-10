package com.splitpaisa.model

import java.time.LocalDate
import java.util.UUID

data class Person(
    val id: String = UUID.randomUUID().toString(),
    val name: String
)

data class Party(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val memberIds: List<String> = emptyList()
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
