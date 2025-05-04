package com.hamzaazman.birthdayreminder.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTodayBirthdaysUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(): Flow<List<Person>> = personRepository.getPersons()
        .map { list ->
            val today = LocalDate.now()
            list.filter {
                it.birthDate.month == today.month && it.birthDate.dayOfMonth == today.dayOfMonth
            }
        }.flowOn(Dispatchers.IO)
}