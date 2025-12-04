package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.backend.MetaManager

class MetasActivity : AppCompatActivity() {

    private lateinit var metaManager: MetaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metas)

        metaManager = MetaManager(this)

        // ===== BOTÕES =====
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener { finish() }

        // ===== PEGAR ELEMENTOS DA TELA =====
        val progressoPeso = findViewById<ProgressBar>(R.id.progressPeso)
        val progressoTreino = findViewById<ProgressBar>(R.id.progressTreinos)
        val progressoAlimentacao = findViewById<ProgressBar>(R.id.progressAlimentacao)

        val txtPesoAtual = findViewById<TextView>(R.id.txtPesoAtual)
        val txtTreinosFeitos = findViewById<TextView>(R.id.txtTreinosFeitos)
        val txtAlimentacao = findViewById<TextView>(R.id.txtAlimentacao)

        // ===== ATUALIZAR TELA =====
        txtPesoAtual.text = "Atual: ${metaManager.getPesoAtual()} kg"
        txtTreinosFeitos.text = "Realizados: ${metaManager.getTreinosFeitos()}"
        txtAlimentacao.text = "Cumprimento: ${metaManager.getAlimentacao()}%"

        progressoPeso.progress = metaManager.getProgressoPeso()
        progressoTreino.progress = metaManager.getProgressoTreino()
        progressoAlimentacao.progress = metaManager.getAlimentacao()
    }
}
