package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

import android.content.Intent
import android.os.Handler
import android.os.Looper


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val texto = findViewById<TextView>(R.id.titulo)
        val palavra = "FireFit"
        val span = SpannableString(palavra)

        // Text dividido 2 cores
        // "Fire" = #FF4601
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF4601")),
            0, 4,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // "Fit" = #FFFFFF
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFFFFF")),
            4, palavra.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //Timer para passar a tela
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500) // 1,5 segundos de espera

        texto.text = span
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}