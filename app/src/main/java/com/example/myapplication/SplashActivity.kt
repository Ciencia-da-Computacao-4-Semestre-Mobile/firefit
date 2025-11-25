package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val texto = findViewById<TextView>(R.id.titulo)
        val palavra = "FireFit"
        val span = SpannableString(palavra)

        // FIRE color (#FF4601)
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF4601")),
            0, 4,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // FIT color (#FFFFFF)
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFFFFF")),
            4, palavra.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        texto.text = span

        // Aguarda 1.5s e verifica login
        Handler(Looper.getMainLooper()).postDelayed({

            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                // Usuário JÁ ESTÁ logado → ir direto pra Home
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                // Não está logado → tela de Login
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()

        }, 1500)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
