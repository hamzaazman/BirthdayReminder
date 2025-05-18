package com.hamzaazman.birthdayreminder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Person(
    val id: Int = 0,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String?,
    val note: String?,
    val profileImageUri: String?
) : Parcelable
