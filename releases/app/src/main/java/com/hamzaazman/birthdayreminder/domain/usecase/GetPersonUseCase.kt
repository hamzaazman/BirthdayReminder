package com.hamzaazman.birthdayreminder.domain.usecase

import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    operator fun invoke(): Flow<List<Person>> = personRepository.getPersons().flowOn(Dispatchers.IO)
}