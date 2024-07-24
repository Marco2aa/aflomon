package com.aflokkat.dice.activite

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aflokkat.dice.R
import com.aflokkat.dice.classe.Aflomon
import com.aflokkat.dice.classe.Attaque

class CombatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_combat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val aflomonMoi = intent.getSerializableExtra("aflomon") as Aflomon

        

        val tv_pv_moi: TextView = findViewById(R.id.tv_pv_moi)
        val tv_nom_moi: TextView = findViewById(R.id.tv_nom_moi)
        val iv_moi: ImageView = findViewById(R.id.iv_aflomon_moi)
        val pb_pv_moi: ProgressBar = findViewById(R.id.pb_pv_moi)

        tv_nom_moi.text = aflomonMoi.nom
        tv_pv_moi.text = "${aflomonMoi.pv}/${aflomonMoi.pv}"

        iv_moi.setImageDrawable(ContextCompat.getDrawable(this, aflomonMoi.idImage))


        val gridView: GridView = findViewById(R.id.gridview)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, aflomonMoi.attaques)

        gridView.adapter = adapter

        val carapupuce = Aflomon(
            "Carapupuce",
            50,
            listOf(
                Attaque("griffe", 5),
                Attaque("KoudBoule", 2),
                Attaque("Pistolet a O", 10),
                Attaque("Rugissement", 3)
            ),
            R.drawable.carapuce
        )

        val tv_pv_ennemi: TextView = findViewById(R.id.tv_pv_ennemi)
        val tv_nom_ennemi: TextView = findViewById(R.id.tv_nom_ennemi)
        val iv_ennemi: ImageView = findViewById(R.id.iv_aflomon_ennemi)
        val pb_pv_ennemi: ProgressBar = findViewById(R.id.pb_pv_ennemi)

        tv_nom_ennemi.text = carapupuce.nom
        tv_pv_ennemi.text = "${carapupuce.pv}/${carapupuce.pv}"

        val tv_victoire : TextView = findViewById(R.id.tv_victoire)
        val bt_rejouer : Button = findViewById(R.id.button4)

        bt_rejouer.setOnClickListener {
            tv_victoire.text = ""
            bt_rejouer.visibility = View.INVISIBLE
            aflomonMoi.pv = aflomonMoi.pvMax
            carapupuce.pv = carapupuce.pvMax
            tv_pv_moi.text = "${aflomonMoi.pv}/${aflomonMoi.pvMax}"
            tv_pv_ennemi.text = "${carapupuce.pv}/${carapupuce.pvMax}"
            pb_pv_moi.progress = 100
            pb_pv_ennemi.progress = 100

        }

        gridView.setOnItemClickListener { parent, view, position, id ->
            if (aflomonMoi.pv > 0 && carapupuce.pv > 0) {
                val attaque = aflomonMoi.attaques[position]
                Toast.makeText(
                    this,
                    "Attaque: ${attaque.nom},Puissance: ${attaque.puissance}",
                    Toast.LENGTH_SHORT
                ).show()
                combat(attaque, carapupuce, pb_pv_ennemi, tv_pv_ennemi)
                if (carapupuce.pv > 0) {
                    val attaqueCarapupuce = carapupuce.attaques.random()
                    Toast.makeText(
                        this,
                        "Cara pupuce Attaque: ${attaqueCarapupuce.nom},Puissance: ${attaqueCarapupuce.puissance}",
                        Toast.LENGTH_SHORT
                    ).show()
                    combat(attaqueCarapupuce, aflomonMoi, pb_pv_moi, tv_pv_moi)
                    if (aflomonMoi.pv == 0) {
                        tv_victoire.text =
                            "${carapupuce.nom} a gagné le combat, ${aflomonMoi.nom} a perdu"
                        bt_rejouer.visibility = View.VISIBLE
                    }

                }
                else{
                    tv_victoire.text =
                        "${aflomonMoi.nom} a gagné le combat, ${carapupuce.nom} a perdu"
                    bt_rejouer.visibility = View.VISIBLE
                }
            }

    }}
}

fun combat( attaque: Attaque,  aflomon: Aflomon, pb_pv: ProgressBar, tv_pv:TextView){
    val newPv = aflomon.pv - attaque.puissance
    aflomon.pv = Math.max(0,newPv)
    val ratio = (aflomon.pv.toDouble() / aflomon.pvMax.toDouble()) * 100
    tv_pv.text = "${aflomon.pv}/${aflomon.pvMax}"
    pb_pv.progress=ratio.toInt()

}