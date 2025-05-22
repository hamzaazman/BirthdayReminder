package com.hamzaazman.birthdayreminder.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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


suspend fun Context.saveResizedImageToInternalStorage(
    uri: Uri,
    maxSize: Int = 512
): String? = withContext(Dispatchers.IO) {
    try {
        val inputStream = contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val ratio = originalBitmap.width.toFloat() / originalBitmap.height
        val (newWidth, newHeight) = if (ratio > 1) {
            maxSize to (maxSize / ratio).toInt()
        } else {
            (maxSize * ratio).toInt() to maxSize
        }

        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        val outputStream = FileOutputStream(file)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        return@withContext file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
