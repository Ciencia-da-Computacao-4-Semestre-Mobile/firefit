package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Remove a barra branca do Android (navigation bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.fire_background)

        setContentView(R.layout.activity_workouts)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_training

        // 🔥 Deixar o item "Treinos" selecionado
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_training -> {
                    if (this !is WorkoutsActivity) {
                        startActivity(Intent(this, WorkoutsActivity::class.java))
                        overridePendingTransition(0,0)
                        finish()
                    }
                    true
                }

                R.id.nav_home -> {
                    if (this !is HomeActivity) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0,0)
                        finish()
                    }
                    true
                }

                R.id.nav_user -> {
                    if (this !is UserActivity) {
                        startActivity(Intent(this, UserActivity::class.java))
                        overridePendingTransition(0,0)
                        finish()
                    }
                    true
                }

                else -> false
            }
        }

    }
}

