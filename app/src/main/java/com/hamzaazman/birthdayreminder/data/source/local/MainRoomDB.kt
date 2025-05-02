package com.hamzaazman.birthdayreminder.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hamzaazman.birthdayreminder.data.model.PersonEntity

@Database(entities = [PersonEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MainRoomDB : RoomDatabase() {
    abstract fun mainDao(): PersonDao
}