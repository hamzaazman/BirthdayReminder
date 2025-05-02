package com.hamzaazman.birthdayreminder.di

import com.hamzaazman.birthdayreminder.data.repository.PersonRepositoryImpl
import com.hamzaazman.birthdayreminder.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMainRepository(repositoryImpl: PersonRepositoryImpl): PersonRepository
}