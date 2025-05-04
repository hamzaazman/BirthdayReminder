package com.hamzaazman.birthdayreminder.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.birthdayreminder.data.source.local.toEntity
import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {


    fun savePerson(person: Person, onComplete: () -> Unit) {
        viewModelScope.launch {
            if (person.id == 0) {
                personRepository.addPerson(person)
            } else {
                personRepository.update(person)
            }
            onComplete()
        }
    }
}