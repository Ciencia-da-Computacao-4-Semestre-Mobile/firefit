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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val texto = findViewById<TextView>(R.id.titulo)
        val palavra = "FireFit"
        val span = SpannableString(palavra)

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

        texto.text = span
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}