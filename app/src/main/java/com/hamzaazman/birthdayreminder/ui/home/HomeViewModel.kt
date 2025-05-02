package com.hamzaazman.birthdayreminder.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.birthdayreminder.domain.usecase.GetPersonUseCase
import com.hamzaazman.birthdayreminder.domain.usecase.GetTodayBirthdaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getPersonUseCase: GetPersonUseCase,
    getTodayBirthdaysUseCase: GetTodayBirthdaysUseCase,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        getPersonUseCase(), getTodayBirthdaysUseCase()
    ) { all, today ->
        HomeUiState(
            persons = all,
            todayBirthdays = today,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )

}