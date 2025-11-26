package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {

    private lateinit var txtUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // TextView do nome
        txtUserName = findViewById(R.id.txtUserName)

        // Pega usuário logado
        val user = auth.currentUser
        val nome = user?.displayName

        // Exibe o nome
        if (!nome.isNullOrEmpty()) {
            txtUserName.text = nome
        } else {
            txtUserName.text = "Usuário"
        }

        // --- Bottom Nav ---
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val btnLogout = findViewById<TextView>(R.id.txtDesconectar)

        bottomNav.selectedItemId = R.id.nav_user

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

        // --- Logout ---
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
