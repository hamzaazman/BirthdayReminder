package com.hamzaazman.birthdayreminder.data.repository

import com.hamzaazman.birthdayreminder.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainDao: MainDao,
) : MainRepository