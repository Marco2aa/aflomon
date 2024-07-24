package com.aflokkat.dice.classe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Attaque(@PrimaryKey val nom : String ,
                   @ColumnInfo("puissance")  val puissance : Int):Serializable {
    override fun toString(): String {
        return nom
    }
}