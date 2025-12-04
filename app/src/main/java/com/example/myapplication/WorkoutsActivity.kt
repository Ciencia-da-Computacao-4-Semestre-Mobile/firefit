package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ImageView

class WorkoutsActivity : AppCompatActivity() {

    private lateinit var card1: FrameLayout
    private lateinit var card2: FrameLayout
    private lateinit var progress1: ProgressBar
    private lateinit var scrollView: ScrollView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.fire_background)

        setContentView(R.layout.activity_workouts)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_training

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

        // Cards
        card1 = findViewById(R.id.card1)
        card2 = findViewById(R.id.card2)

        card1.setOnClickListener { openDetails("Treino A") }
        card2.setOnClickListener { openDetails("Treino B") }

        animateCard(card1)
        animateCard(card2)

        // Progresso
        progress1 = findViewById(R.id.progresso)

        prefs = getSharedPreferences("progresso", MODE_PRIVATE)
        val savedValue = prefs.getInt("card1_progress", 70)
        animateProgress(progress1, savedValue)

        // ---------- SEUS BOTÕES ----------
        findViewById<Button>(R.id.btnIniciarCardio).setOnClickListener {
            startActivity(Intent(this, CardioActivity::class.java))
        }

        findViewById<Button>(R.id.btnIniciarCardio1).setOnClickListener {
            startActivity(Intent(this, CardioActivity::class.java))
        }

        // 🔥 NOVO: Botão HIIT
        findViewById<Button>(R.id.btnHiit).setOnClickListener {
            startActivity(Intent(this, HiitActivity::class.java))
        }
    }

    private fun openDetails(title: String) { }

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
