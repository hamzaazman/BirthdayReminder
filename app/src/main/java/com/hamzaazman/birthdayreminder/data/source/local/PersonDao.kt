package com.hamzaazman.birthdayreminder.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hamzaazman.birthdayreminder.data.model.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Query("SELECT * FROM persons")
    fun getAllPerson(): Flow<List<PersonEntity>>

    @Update
    suspend fun update(person: PersonEntity)

    @Query("DELETE FROM persons WHERE id = :id")
    suspend fun deletePerson(id: Int)
}
