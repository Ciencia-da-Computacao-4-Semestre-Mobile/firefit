package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

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

        // 🔥 TextView onde mostrará o nome (ALTERE o id se no seu XML for outro)
        tvUserName = findViewById(R.id.tvUserName)

        // 🔥 Recupera o nome salvo no Firebase Auth
        val user = auth.currentUser
        val nome = user?.displayName

        if (!nome.isNullOrEmpty()) {
            tvUserName.text = "$nome!"
        } else {
            tvUserName.text = ""
        }

        // 🔥 Navegação inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Seleciona o item atual da tela
        bottomNav.selectedItemId = R.id.nav_home

        // Listener para trocar de tela
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

                else -> false
            }
        }
    }
}
