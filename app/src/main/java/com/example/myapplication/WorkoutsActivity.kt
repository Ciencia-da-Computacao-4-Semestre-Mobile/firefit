package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkoutsActivity : AppCompatActivity() {

    private var card1: FrameLayout? = null
    private var card2: FrameLayout? = null
    private var progress1: ProgressBar? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.fire_background)

        setContentView(R.layout.activity_workouts)

        // Bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.selectedItemId = R.id.nav_training
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { startActivitySafe(HomeActivity::class.java); true }
                R.id.nav_training -> { startActivitySafe(WorkoutsActivity::class.java); true }
                R.id.nav_user -> { startActivitySafe(UserActivity::class.java); true }
                R.id.nav_events -> { startActivitySafe(ScheduledEventsActivity::class.java); true }
                else -> false
            }
        }

        // Cards
        card1 = findViewById(R.id.card1)
        card2 = findViewById(R.id.card2)

        card1?.setOnClickListener { openDetails("Treino A") }
        card2?.setOnClickListener { openDetails("Treino B") }

        card1?.let { animateCard(it) }
        card2?.let { animateCard(it) }

        // Progresso
        progress1 = findViewById(R.id.progresso)
        prefs = getSharedPreferences("progresso", MODE_PRIVATE)
        val savedValue = prefs.getInt("card1_progress", 70)
        progress1?.let { animateProgress(it, savedValue) }

        // BOTÕES DO SEU CÓDIGO (ambos unificados)
        findViewById<Button>(R.id.btnIniciarCardio)?.setOnClickListener {
            startActivity(Intent(this, CardioActivity::class.java))
        }

        findViewById<Button>(R.id.btnIniciarCardio1)?.setOnClickListener {
            startActivity(Intent(this, ForcaTotalActivity::class.java))
        }

        findViewById<Button>(R.id.btnHiit)?.setOnClickListener {
            startActivity(Intent(this, HiitActivity::class.java))
        }

    }

    private fun startActivitySafe(activityClass: Class<*>) {
        if (!this::class.java.isAssignableFrom(activityClass)) {
            startActivity(Intent(this, activityClass))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun openDetails(title: String) {
        // TODO: abrir detalhes futuramente
    }

    private fun animateCard(card: FrameLayout) {
        card.alpha = 0f
        card.translationY = 40f
        card.animate()
            .alpha(1f)
            .translationYBy(-40f)
            .setDuration(600)
            .start()
    }

    private fun animateProgress(bar: ProgressBar, value: Int) {
        val anim = ObjectAnimator.ofInt(bar, "progress", 0, value)
        anim.duration = 800
        anim.start()
    }
}
