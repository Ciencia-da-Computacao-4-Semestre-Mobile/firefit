package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
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

        // ==========================
        // FIREBASE
        // ==========================
        auth = FirebaseAuth.getInstance()

        // ==========================
        // TEXTVIEW DO NOME
        // ==========================
        txtUserName = findViewById(R.id.txtUserName)

        val user = auth.currentUser
        val nome = user?.displayName

        txtUserName.text = if (!nome.isNullOrEmpty()) nome else "Usuário"

        // ==========================
        // BOTTOM NAV
        // ==========================
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

                R.id.nav_user -> true

                else -> false
            }
        }

        // ==========================
        // LOGOUT
        // ==========================
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        // ==========================
        // CARDS CLICÁVEIS
        // ==========================
        val cardDadosPessoais = findViewById<LinearLayout>(R.id.cardPersonalData)
        val cardTreinos = findViewById<LinearLayout>(R.id.cardWorkouts)
        val cardEventos = findViewById<LinearLayout>(R.id.cardEvents)

        cardDadosPessoais.setOnClickListener {
            startActivity(Intent(this, PersonalDataActivity::class.java))
        }

        cardTreinos.setOnClickListener {
            startActivity(Intent(this, WorkoutsSavedActivity::class.java))
        }

        cardEventos.setOnClickListener {
            startActivity(Intent(this, MyEventsActivity::class.java))
        }
    }
}
