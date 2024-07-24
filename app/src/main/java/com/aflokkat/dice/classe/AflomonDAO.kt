package com.aflokkat.dice.classe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AflomonDAO {
    @Query("SELECT * FROM aflomon")
    fun getAll(): List<Aflomon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg aflomon: Aflomon)

    @Delete
    fun delete(aflomon: Aflomon)


}