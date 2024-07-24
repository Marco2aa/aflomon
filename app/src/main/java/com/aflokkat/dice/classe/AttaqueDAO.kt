package com.aflokkat.dice.classe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AttaqueDAO {
    @Query("SELECT * FROM attaque")
    fun getAll(): List<Attaque>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg attaques:Attaque)

}