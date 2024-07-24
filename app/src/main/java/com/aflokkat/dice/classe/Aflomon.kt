package com.aflokkat.dice.classe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity
@TypeConverters(Converters::class)
data class Aflomon(@PrimaryKey val nom : String,
                   @ColumnInfo("pv_max") val pvMax : Int,
                   @ColumnInfo("attaques") val attaques : List<Attaque>,
                   @ColumnInfo("id_image") val idImage: Int):Serializable {
    var pv by mutableStateOf(pvMax)
    override fun toString(): String {
        return nom
    }

    constructor(nom: String, pvMax: Int,pv: Int, attaques: List<Attaque>, idImage: Int) : this(nom, pvMax, attaques, idImage){
        this.pv = pv
    }
}