package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio)

        // IDs exatamente como no seu XML
        val group = findViewById<RadioGroup>(R.id.groupCardio)
        val btnIniciar = findViewById<Button>(R.id.btnIniciarTreino)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        btnIniciar.setOnClickListener {
            val selectedId = group.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Selecione um tipo de cardio!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (selectedId) {
                R.id.rbCorrida -> {
                    val intent = Intent(this, CardioIntensityActivity::class.java)
                    intent.putExtra("tipoCardio", "corrida")
                    startActivity(intent)
                }
                R.id.rbBicicleta -> {
                    val intent = Intent(this, CardioIntensityActivity::class.java)
                    intent.putExtra("tipoCardio", "bicicleta")
                    startActivity(intent)
                }
                R.id.rbCaminhada -> {
                    val intent = Intent(this, CardioIntensityActivity::class.java)
                    intent.putExtra("tipoCardio", "caminhada")
                    startActivity(intent)
                }
                else -> Toast.makeText(this, "Opção inválida", Toast.LENGTH_SHORT).show()
            }
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_training

        // 🔥 Deixar o item "Treinos" selecionado
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    if (this !is HomeActivity) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_training -> {
                    if (this !is WorkoutsActivity) {
                        startActivity(Intent(this, WorkoutsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_user -> {
                    if (this !is UserActivity) {
                        startActivity(Intent(this, UserActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_events -> {
                    if (this !is ScheduledEventsActivity) {
                        startActivity(Intent(this, ScheduledEventsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                else -> false
            }
        }
    }
}
