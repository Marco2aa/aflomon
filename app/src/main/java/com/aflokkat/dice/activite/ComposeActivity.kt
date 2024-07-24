package com.aflokkat.dice.activite

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.aflokkat.dice.R
import com.aflokkat.dice.activite.ui.theme.DiceTheme
import com.aflokkat.dice.classe.Aflomon
import com.aflokkat.dice.classe.AppDatabase
import com.aflokkat.dice.classe.Attaque
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

class ComposeActivity : ComponentActivity() {
    private lateinit var myAflomonList: List<Aflomon>
    private lateinit var foeAflomonList: List<Aflomon>
    private var myCurrentIndex = 0
    private var foeCurrentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "aflodb"
            ).fallbackToDestructiveMigration().build()

            val attaqueDao = db.attaqueDao()
            val aflomonDao = db.aflomonDao()

            val fakeAttaques = listOf(
                Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                Attaque("Pistolet à eau", 10), Attaque("Ultralaser", 25), Attaque("HydroCanon", 15),
                Attaque("Bulle d'eau", 2), Attaque("Lance-Soleil", 22), Attaque("Surf", 2)
            )

            foeAflomonList = listOf(
                Aflomon("Carapuce", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Flamèche", 2),
                    Attaque("Lance-flamme", 10), Attaque("Rugissement", 3)
                ), R.drawable.carapuce),
                Aflomon("Chenipan", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Rugissement", 3)
                ), R.drawable.chenipan),
                Aflomon("Ratata", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Rugissement", 3)
                ), R.drawable.rattata),
                Aflomon("Nidoran_f", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Rugissement", 3)
                ), R.drawable.nidoran_m),
                Aflomon("Melofee", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Rugissement", 3)
                ), R.drawable.melofee),
                Aflomon("Aspicot", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Rugissement", 3)
                ), R.drawable.aspicot)
            )

            myAflomonList = aflomonDao.getAll()

            if (attaqueDao.getAll().isEmpty()) {
                attaqueDao.insertAll(*fakeAttaques.toTypedArray())
            }

            println(aflomonDao.getAll())
            println(foeAflomonList)

            withContext(Dispatchers.Main) {
                enableEdgeToEdge()
                setContent {
                    DiceTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MainScene(
                                myAflomonList = myAflomonList,
                                foeAflomonList = foeAflomonList
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScene(myAflomonList: List<Aflomon>, foeAflomonList: List<Aflomon>) {
    var texteCombat by remember { mutableStateOf("") }
    var myCurrentIndex by remember { mutableStateOf(0) }
    var foeCurrentIndex by remember { mutableStateOf(0) }
    var showPokemonSelection by remember { mutableStateOf(false) } // Nouvelle variable d'état

    if (myCurrentIndex >= myAflomonList.size || foeCurrentIndex >= foeAflomonList.size) {
        texteCombat = "Combat terminé"
        return
    }

    val myAflomon = myAflomonList[myCurrentIndex]
    val foeAflomon = foeAflomonList[foeCurrentIndex]
    val aliveAflomon = myAflomonList.filter { it.pv > 0 }

    if (myAflomon == null || foeAflomon == null) {
        texteCombat = "Combat terminé"
        return
    }
    texteCombat= ""

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(20.dp))
        Row {
            Spacer(Modifier.weight(1f))
            AflomonUI(aflomon = foeAflomon)
            Spacer(Modifier.width(20.dp))
        }
        Spacer(Modifier.weight(1f))
        Text(texteCombat, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        if (showPokemonSelection) {
            LazyColumn {
                items(aliveAflomon) { aflomon ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                myCurrentIndex = myAflomonList.indexOf(aflomon)
                                showPokemonSelection = false
                            }
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = aflomon.idImage),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(aflomon.nom)
                    }
                }
            }
        } else {
            Row {
                Spacer(Modifier.weight(1f))
                AflomonUI(aflomon = myAflomon)
                Spacer(modifier = Modifier.width(10.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(myAflomon.attaques) { attaque ->
                        val context = LocalContext.current
                        if (myAflomon.pv > 0 && foeAflomon.pv > 0) {
                            Text(attaque.nom, Modifier.clickable {
                                Toast.makeText(context, "Attaque : ${attaque.nom} Puissance : ${attaque.puissance}", Toast.LENGTH_SHORT).show()
                                foeAflomon.pv = max(0, foeAflomon.pv - attaque.puissance)

                                if (foeAflomon.pv > 0) {
                                    val attaqueFoe = foeAflomon.attaques.random()
                                    myAflomon.pv = max(0, myAflomon.pv - attaqueFoe.puissance)
                                    if (myAflomon.pv == 0) {
                                        texteCombat = "${myAflomon.nom} est mort, remplacement par le suivant"
                                        myCurrentIndex++
                                    }
                                } else {
                                    texteCombat = "${foeAflomon.nom} est mort, remplacement par le suivant"
                                    foeCurrentIndex++
                                }
                            })
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(20.dp))


        Button(onClick = {
            if (showPokemonSelection) {
                showPokemonSelection = false
            } else {
                showPokemonSelection = true
            }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(if (showPokemonSelection) "Retour aux Attaques" else "Changer de Pokémon")
        }
    }
}

@Composable
fun AflomonUI(aflomon: Aflomon, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val ratio = aflomon.pv.toFloat() / aflomon.pvMax
        val animatedProgress by animateFloatAsState(
            targetValue = ratio,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        Image(
            painter = painterResource(id = aflomon.idImage),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = aflomon.nom,
            modifier = modifier
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            color = Color.Green,
            trackColor = Color.Gray,
            modifier = Modifier.width(50.dp)
        )
        Text("${aflomon.pv}/${aflomon.pvMax}")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiceTheme {
        MainScene(
            myAflomonList = listOf(
                Aflomon("Salamon", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Bulle d'eau", 2),
                    Attaque("Pistolet à eau", 10), Attaque("Ultralaser", 25)
                ), R.drawable.salameche)
            ),
            foeAflomonList = listOf(
                Aflomon("Carapuce", 50, 50, listOf(
                    Attaque("Griffe", 5), Attaque("Flamèche", 2),
                    Attaque("Lance-flamme", 10), Attaque("Rugissement", 3)
                ), R.drawable.carapuce)
            )
        )
    }
}
