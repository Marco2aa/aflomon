package com.aflokkat.dice.activite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aflokkat.dice.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bt6face : Button = findViewById(R.id.button)
        val bt20face : Button = findViewById(R.id.button2)

        bt6face.setOnClickListener {
            val intent = Intent(this, LaunchActivity::class.java)
            intent.putExtra("lancer",6)
            startActivity(intent)
        }

        bt20face.setOnClickListener {
            val intent = Intent(this, LaunchActivity::class.java)
            intent.putExtra("lancer",20)
            startActivity(intent)
        }

    }
}