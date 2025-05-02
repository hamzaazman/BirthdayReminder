package com.hamzaazman.birthdayreminder.domain.model

import java.time.LocalDate

data class Person(
    val id: Int = 0,
    val name: String,
    val birthDate: LocalDate
)
