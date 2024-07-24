package com.aflokkat.dice.activite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aflokkat.dice.R
import com.aflokkat.dice.classe.Aflomon
import com.aflokkat.dice.classe.AflomonAdapter
import com.aflokkat.dice.classe.Attaque

class EquipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_equipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rv_equipe: RecyclerView = findViewById(R.id.rv_equipe)
        rv_equipe.layoutManager = LinearLayoutManager(this)

        val aflomons = listOf(Aflomon(
            "Carapuce",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Pistolet a O", 10),
                Attaque("Rugissement", 3)
            ),
            R.drawable.carapuce
        ),Aflomon(
            "Salamèche",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Flammèche", 10),
                Attaque("Rugissement", 3)
            ),R.drawable.salameche
        ),Aflomon(
            "Pikachu",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Eclair", 10),
                Attaque("Rugissement", 3)
            ),R.drawable.pikachu
        ),Aflomon(
            "Florizarre",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Lance Soleil", 10),
                Attaque("Rugissement", 3)
            ),R.drawable.florizarre
        ),Aflomon(
            "Goupix",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Lance Flamme", 10),
                Attaque("Rugissement", 3)
            ),R.drawable.goupix))


        val adapter = AflomonAdapter(aflomons.toTypedArray())
        rv_equipe.adapter = adapter

        val button_combat: Button = findViewById(R.id.button5)

        button_combat.setOnClickListener {
            val intent = Intent(this, ComposeActivity::class.java)
            startActivity(intent)
        }




    }
}