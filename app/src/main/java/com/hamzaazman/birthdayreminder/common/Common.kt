package com.hamzaazman.birthdayreminder.common

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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


@RequiresApi(Build.VERSION_CODES.O)
fun MaterialDatePicker.Builder<Long>.setSelectedDateOrToday(selectedDate: LocalDate?): MaterialDatePicker.Builder<Long> {
    val zoneId = ZoneId.systemDefault()
    val selectedMillis = selectedDate
        ?.atTime(12, 0)
        ?.atZone(zoneId)
        ?.withZoneSameInstant(ZoneOffset.UTC)
        ?.toInstant()
        ?.toEpochMilli()
        ?: MaterialDatePicker.todayInUtcMilliseconds()
    return this.setSelection(selectedMillis)
}

@RequiresApi(Build.VERSION_CODES.O)
fun createBirthDatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
): MaterialDatePicker<Long> {
    val picker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Doğum Tarihi Seç")
        .setSelectedDateOrToday(selectedDate)
        .build()

    picker.addOnPositiveButtonClickListener { selection ->
        val instant = Instant.ofEpochMilli(selection)
        val selected = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
        onDateSelected(selected)
    }

    return picker
}

fun TextInputEditText.setupDatePicker(onPick: () -> Unit) {
    setOnClickListener { onPick() }
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            onPick()
            clearFocus()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val TurkishDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")


val LocalDate.formatTurkish: String
    @RequiresApi(Build.VERSION_CODES.O)
    get() = format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
