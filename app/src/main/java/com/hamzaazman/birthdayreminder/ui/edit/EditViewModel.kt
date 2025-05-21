package com.hamzaazman.birthdayreminder.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    private val _person = MutableStateFlow<Person?>(null)
    val person: StateFlow<Person?> = _person.asStateFlow()

    fun getPersonById(id: Int, onComplete: (Person?) -> Unit) {
        viewModelScope.launch {
            personRepository.getPersonById(id).collect { person ->
                _person.value = person
                onComplete(person)
            }
        }
    }

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

    fun deletePerson(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            personRepository.deletePerson(id)
            onComplete()
        }
    }
}