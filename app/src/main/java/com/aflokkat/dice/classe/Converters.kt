package com.aflokkat.dice.classe

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGroupTaskMemberList(value: List<Attaque>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Attaque>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGroupTaskMemberList(value: String): List<Attaque> {
        val gson = Gson()
        val type = object : TypeToken<List<Attaque>>() {}.type
        return gson.fromJson(value, type)
    }
}