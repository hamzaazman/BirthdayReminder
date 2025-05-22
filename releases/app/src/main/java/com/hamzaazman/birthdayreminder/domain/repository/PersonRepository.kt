package com.hamzaazman.birthdayreminder.domain.repository

import com.hamzaazman.birthdayreminder.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun addPerson(person: Person)
    fun getPersons(): Flow<List<Person>>
    suspend fun getPersonById(id: Int): Flow<Person?>
    suspend fun update(person: Person)
    suspend fun deletePerson(id: Int)
}
