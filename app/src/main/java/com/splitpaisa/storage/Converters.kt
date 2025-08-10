package com.splitpaisa.storage

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toStringList(csv: String?): List<String>? =
        csv?.takeIf { it.isNotBlank() }?.split(",") ?: emptyList()
}
