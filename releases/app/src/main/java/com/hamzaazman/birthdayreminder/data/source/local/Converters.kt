package com.hamzaazman.birthdayreminder.data.source.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromString(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun localDateToString(date: LocalDate): String = date.toString()
}