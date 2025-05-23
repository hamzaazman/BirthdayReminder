package com.hamzaazman.birthdayreminder.data.source.local

import com.hamzaazman.birthdayreminder.data.model.PersonEntity
import com.hamzaazman.birthdayreminder.domain.model.Person


fun Person.toEntity() = PersonEntity(id, name, birthDate, phoneNumber, note, profileImageUri)
fun PersonEntity.toDomain() = Person(id, name, birthDate, phoneNumber, note, profileImageUri)
