package com.aflokkat.dice.classe

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ Attaque::class, Aflomon::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aflomonDao(): AflomonDAO
    abstract fun attaqueDao(): AttaqueDAO
}