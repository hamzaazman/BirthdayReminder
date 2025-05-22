package com.hamzaazman.birthdayreminder.ui.home

import com.hamzaazman.birthdayreminder.domain.model.Person

data class HomeUiState(
    val persons: List<Person> = emptyList(),
    val todayBirthdays: List<Person> = emptyList(),
    val isLoading: Boolean = true,
) {
    val hasTodayBirthdays: Boolean
        get() = todayBirthdays.isNotEmpty()

    val hasAllBirthdays: Boolean
        get() = persons.isNotEmpty()
}