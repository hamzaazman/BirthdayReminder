package com.hamzaazman.birthdayreminder.data.repository

import com.hamzaazman.birthdayreminder.data.source.local.PersonDao
import com.hamzaazman.birthdayreminder.data.source.local.toDomain
import com.hamzaazman.birthdayreminder.data.source.local.toEntity
import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PersonRepositoryImpl @javax.inject.Inject constructor(
    private val personDao: PersonDao,
) : PersonRepository {
    override suspend fun addPerson(person: Person) {
        personDao.insertPerson(person.toEntity())
    }

    override fun getPersons(): Flow<List<Person>> {
        return personDao.getAllPerson().map { list ->
            list.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getPersonById(id: Int): Flow<Person?> = flow {
        emit(personDao.getPersonById(id)?.toDomain())
    }

    override suspend fun update(person: Person) {
        personDao.update(person.toEntity())
    }

    override suspend fun deletePerson(id: Int) {
        personDao.deletePerson(id)
    }
}