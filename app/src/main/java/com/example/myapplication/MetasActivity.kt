package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MetasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metas) // <-- coloque o nome do seu XML aqui

        // ===== BOTÕES =====
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnAdicionarMeta = findViewById<TextView>(R.id.btnAdicionarMeta)

        // ===== VOLTAR =====
        btnVoltar.setOnClickListener {
            finish() // fecha e volta para a anterior
        }

        // ===== ADICIONAR NOVA META =====
        btnAdicionarMeta.setOnClickListener {
            // coloque sua Activity que vai cadastrar uma meta
            // Exemplo:
            // startActivity(Intent(this, NovaMetaActivity::class.java))
        }
    }
}
