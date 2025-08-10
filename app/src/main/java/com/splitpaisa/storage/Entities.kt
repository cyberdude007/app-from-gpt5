package com.splitpaisa.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val id: String,
    val name: String
)

@Entity(tableName = "parties")
data class PartyEntity(
    @PrimaryKey val id: String,
    val name: String,
    /** Comma-separated personIds */
    val memberIdsCsv: String
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val partyId: String,
    val amount: Double,
    val description: String,
    val payerId: String,
    /** Comma-separated personIds */
    val participantIdsCsv: String,
    /** LocalDate stored as epochDay */
    val dateEpochDay: Long
)
