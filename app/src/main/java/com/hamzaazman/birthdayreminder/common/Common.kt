package com.hamzaazman.birthdayreminder.common

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun View.setVisibleIf(condition: Boolean) {
    if (condition && visibility != View.VISIBLE) visibility = View.VISIBLE
    else if (!condition && visibility != View.GONE) visibility = View.GONE
}
fun View.updateVisibilityIfNeeded(show: Boolean) {
    val shouldChange = (show && visibility != View.VISIBLE) || (!show && visibility != View.GONE)
    if (shouldChange) {
        visibility = if (show) View.VISIBLE else View.GONE
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun daysUntilNextBirthday(birthDate: LocalDate): Long {
    val today = LocalDate.now()
    val nextBirthday = birthDate.withYear(today.year).let {
        if (it.isBefore(today) || it.isEqual(today)) it.plusYears(1) else it
    }
    return ChronoUnit.DAYS.between(today, nextBirthday)
}