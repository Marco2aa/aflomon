package com.aflokkat.dice.activite

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aflokkat.dice.R
import kotlin.math.floor

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lancerDe : Button = findViewById(R.id.button3)
        val tv_lancer : TextView = findViewById(R.id.textView)
        lancerDe.setOnClickListener {
            val lancerMax = intent.getIntExtra("lancer", 6)
            val lancer : Double = floor((Math.random()*lancerMax)+1)
            println("le resultat du lancer $lancer")
            tv_lancer.text = "le resultat du lancer $lancer"
        }
    }
}