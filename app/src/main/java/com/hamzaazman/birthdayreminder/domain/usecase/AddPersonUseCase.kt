package com.hamzaazman.birthdayreminder.domain.usecase

import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import javax.inject.Inject

class AddPersonUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    suspend operator fun invoke(person: Person) = personRepository.addPerson(person)
}