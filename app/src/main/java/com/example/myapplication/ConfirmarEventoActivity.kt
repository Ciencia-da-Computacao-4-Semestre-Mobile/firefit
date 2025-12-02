package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ConfirmarEventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_evento)

        val titulo = findViewById<TextView>(R.id.txtTitulo)
        val info = findViewById<TextView>(R.id.txtInfo)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        val nome = intent.getStringExtra("nome")
        val professor = intent.getStringExtra("professor")
        val data = intent.getStringExtra("data")
        val hora = intent.getStringExtra("hora")

        titulo.text = nome
        info.text = "Professor: $professor\nData: $data\nHora: $hora"

        btnConfirmar.setOnClickListener { finish() }
        btnCancelar.setOnClickListener { finish() }
        btnVoltar.setOnClickListener { finish() }
    }
}
