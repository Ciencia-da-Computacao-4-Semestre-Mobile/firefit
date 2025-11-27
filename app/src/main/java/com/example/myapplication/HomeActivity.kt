package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Remove barra branca e ajusta nav bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.fire_background)

        setContentView(R.layout.activity_home)

        // 🔥 Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 🔥 TextView com o nome do usuário
        tvUserName = findViewById(R.id.tvUserName)

        // Recupera o nome configurado no Firebase Auth
        val user = auth.currentUser
        val nome = user?.displayName

        if (!nome.isNullOrEmpty()) {
            tvUserName.text = "$nome!"
        } else {
            tvUserName.text = "Usuario"
        }

        // 🔥 NOVOS ELEMENTOS
        val txtDays = findViewById<TextView>(R.id.txtDays)
        val txtMotivation = findViewById<TextView>(R.id.txtMotivation)

        // Atualiza dias e frase
        updateWeeklyLoginCounter(txtDays)
        showDailyMotivationalPhrase(txtMotivation)

        // Atualiza barra e fogo
        updateWeeklyProgressBar()

        // 🔥 Navegação inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home

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

                //R.id.nav_events -> {
                   // if (this !is EventsActivity) {
                      //  startActivity(Intent(this, EventsActivity::class.java))
                      //  overridePendingTransition(0, 0)
                      //  finish()
                  //  }
                  //  true
                //}

                else -> false
            }
        }

        val tvSeeAllEvents: TextView = findViewById(R.id.tvSeeAllEvents)

       // tvSeeAllEvents.setOnClickListener {
       //     // Abre a tela de Events
         //   val intent = Intent(this, EventsActivity::class.java)
           // startActivity(intent)
        //}
    }

    // -------------------------------------------------------------
    //          🔥 FUNÇÃO 1 — CONTADOR DE DIAS POR SEMANA
    // -------------------------------------------------------------
    private fun updateWeeklyLoginCounter(txtView: TextView) {
        val prefs = getSharedPreferences("app_usage", MODE_PRIVATE)
        val editor = prefs.edit()

        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val savedDay = prefs.getInt("last_login_day", -1)

        var days = prefs.getInt("week_days", 0)

        // Se entrou em um novo dia → +1
        if (today != savedDay) {
            days++
            editor.putInt("week_days", days)
            editor.putInt("last_login_day", today)
            editor.apply()
        }

        txtView.text = "$days /7 dias"
    }

    // -------------------------------------------------------------
    //      🔥 FUNÇÃO 2 — FRASE MOTIVACIONAL ALEATÓRIA DIÁRIA
    // -------------------------------------------------------------
    private fun showDailyMotivationalPhrase(textView: TextView) {

        val phrases = listOf(
            "Você é mais forte do que imagina!",
            "Cada treino te deixa mais perto da sua melhor versão!",
            "O importante é nunca desistir.",
            "Um dia de cada vez. Continue!",
            "A disciplina vence a motivação.",
            "Corpo forte, mente forte!",
            "Você já deu o primeiro passo. Não pare."
        )

        val prefs = getSharedPreferences("daily_phrase", MODE_PRIVATE)
        val editor = prefs.edit()

        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = prefs.getInt("phrase_day", -1)

        val phrase: String

        if (today != lastDay) {
            phrase = phrases.random()
            editor.putString("phrase_text", phrase)
            editor.putInt("phrase_day", today)
            editor.apply()
        } else {
            phrase = prefs.getString("phrase_text", phrases.random())!!
        }

        textView.text = phrase
    }

    // -------------------------------------------------------------
    //        🔥 FUNÇÃO 3 — BAR PROGRESSO + FOGO SE MOVENDO
    // -------------------------------------------------------------
    private fun updateWeeklyProgressBar() {

        val progressBar = findViewById<ProgressBar>(R.id.progressWeek)
        val fire = findViewById<ImageView>(R.id.imgFire)

        val prefs = getSharedPreferences("app_usage", MODE_PRIVATE)
        val days = prefs.getInt("week_days", 0)

        progressBar.max = 7
        progressBar.progress = days

        progressBar.post {

            val barWidth = progressBar.width
            val fireWidth = fire.width

            val percent = days.toFloat() / 7f

            // 🔥 Ajustes MANUAIS feitos por você
            val offsetX = -17f   // mexa aqui (positivo → para direita | negativo → para esquerda)
            val offsetY = -20f   // mexa aqui (valor negativo sobe o fogo)

            val posX = (barWidth - fireWidth) * percent

            fire.translationX = posX + offsetX
            fire.translationY = offsetY

            fire.elevation = 50f
        }
    }
}
