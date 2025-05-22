package com.hamzaazman.birthdayreminder.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "persons")
@Parcelize
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String?,
    val note: String?,
    val profileImageUri: String?
): Parcelable
