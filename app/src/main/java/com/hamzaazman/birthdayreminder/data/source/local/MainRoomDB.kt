package com.hamzaazman.birthdayreminder.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamzaazman.birthdayreminder.data.model.PersonEntity

@Database(entities = [PersonEntity::class], version = 1, exportSchema = false)
abstract class MainRoomDB : RoomDatabase() {
    abstract fun mainDao():
}