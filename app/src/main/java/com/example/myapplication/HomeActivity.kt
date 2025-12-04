package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove barra branca e ajusta nav bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.fire_background)

        setContentView(R.layout.activity_home)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()
        tvUserName = findViewById(R.id.tvUserName)

        val user = auth.currentUser
        val nome = user?.displayName
        tvUserName.text = nome?.let { "$it!" } ?: "Usuario"

        // -----------------------------
        // Configurar cards para enviar dados
        // -----------------------------
        val cardEvent1 = findViewById<MaterialCardView>(R.id.cardEvent1)
        val cardEvent2 = findViewById<MaterialCardView>(R.id.cardEvent2)
        val cardEvent3 = findViewById<MaterialCardView>(R.id.cardEvent3)

        cardEvent1.setOnClickListener {
            val intent = Intent(this, ConfirmarEventoActivity::class.java)
            intent.putExtra("nome", "Funcional")
            intent.putExtra("professor", "Carlos")
            intent.putExtra("data", "07/12/2025")
            intent.putExtra("hora", "09:30")
            intent.putExtra("duracao", "60")
            intent.putExtra("imagemUrl", "event1.webp")
            startActivity(intent)
        }

        cardEvent2.setOnClickListener {
            val intent = Intent(this, ConfirmarEventoActivity::class.java)
            intent.putExtra("nome", "Yoga")
            intent.putExtra("professor", "Ana Paula")
            intent.putExtra("data", "06/12/2025")
            intent.putExtra("hora", "08:00")
            intent.putExtra("duracao", "45")
            intent.putExtra("imagemUrl", "yoga.webp")
            startActivity(intent)
        }

        cardEvent3.setOnClickListener {
            val intent = Intent(this, ConfirmarEventoActivity::class.java)
            intent.putExtra("nome", "Musculação")
            intent.putExtra("professor", "Rodrigo")
            intent.putExtra("data", "05/12/2025")
            intent.putExtra("hora", "14:00")
            intent.putExtra("duracao", "90")
            intent.putExtra("imagemUrl", "crossfit.webp")
            startActivity(intent)
        }

        // -----------------------------
        // Resto do HomeActivity (barra, motivação, progresso)
        // -----------------------------
        val txtDays = findViewById<TextView>(R.id.txtDays)
        val txtMotivation = findViewById<TextView>(R.id.txtMotivation)
        updateWeeklyLoginCounter(txtDays)
        showDailyMotivationalPhrase(txtMotivation)
        updateWeeklyProgressBar()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_training -> { startActivity(Intent(this, WorkoutsActivity::class.java)); true }
                R.id.nav_user -> { startActivity(Intent(this, UserActivity::class.java)); true }
                R.id.nav_events -> { startActivity(Intent(this, ScheduledEventsActivity::class.java)); true }
                else -> false
            }
        }

        findViewById<TextView>(R.id.tvSeeAllEvents).setOnClickListener {
            startActivity(Intent(this, ScheduledEventsActivity::class.java))
        }

        findViewById<TextView>(R.id.txtMetas).setOnClickListener {
            startActivity(Intent(this, MetasActivity::class.java))
        }
    }

    // -----------------------------
    // Mesmas funções de dias, motivação e progresso
    // -----------------------------
    private fun updateWeeklyLoginCounter(txtView: TextView) {
        val prefs = getSharedPreferences("app_usage", MODE_PRIVATE)
        val editor = prefs.edit()
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val savedDay = prefs.getInt("last_login_day", -1)
        var days = prefs.getInt("week_days", 0)
        if (today != savedDay) {
            days++
            editor.putInt("week_days", days)
            editor.putInt("last_login_day", today)
            editor.apply()
        }
        txtView.text = "$days /7 dias"
    }

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

    private fun updateWeeklyProgressBar() {
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.progressWeek)
        val fire = findViewById<ImageView>(R.id.imgFire)
        val prefs = getSharedPreferences("app_usage", MODE_PRIVATE)
        val days = prefs.getInt("week_days", 0)
        progressBar.max = 7
        progressBar.progress = days
        progressBar.post {
            val barWidth = progressBar.width
            val fireWidth = fire.width
            val percent = days.toFloat() / 7f
            val offsetX = -17f
            val offsetY = -20f
            val posX = (barWidth - fireWidth) * percent
            fire.translationX = posX + offsetX
            fire.translationY = offsetY
            fire.elevation = 50f
        }
    }
}
