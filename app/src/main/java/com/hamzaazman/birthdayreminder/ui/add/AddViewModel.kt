package com.hamzaazman.birthdayreminder.ui.add

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.birthdayreminder.common.TurkishDateFormatter
import com.hamzaazman.birthdayreminder.domain.model.Person
import com.hamzaazman.birthdayreminder.domain.usecase.AddPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addPersonUseCase: AddPersonUseCase
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePerson(name: String, birthDateStr: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val date = LocalDate.parse(birthDateStr, TurkishDateFormatter)
                val person = Person(name = name, birthDate = date)
                addPersonUseCase(person)
                onSuccess()
            } catch (e: Exception) {

                Log.e("AddPersonViewModel", "Hata: ${e.message}")
            }
        }
    }
}