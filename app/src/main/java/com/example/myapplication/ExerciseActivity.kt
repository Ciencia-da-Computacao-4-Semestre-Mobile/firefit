package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RECEBENDO OS DADOS DO INTENT
        val nome = intent.getStringExtra("nome") ?: "Exercício"
        val series = intent.getStringExtra("series") ?: "-"
        val repeticoes = intent.getStringExtra("repeticoes") ?: "-"
        val pausa = intent.getStringExtra("pausa") ?: "-"
        val gifUrl = intent.getStringExtra("gifUrl") ?: ""

        // EXIBINDO NA TELA
        binding.txtNome.text = nome
        binding.txtSeries.text = series
        binding.txtRepeticoes.text = repeticoes
        binding.txtPausa.text = "Pausa: $pausa"

        // CARREGANDO GIF (ou imagem) COM GLIDE
        Glide.with(this)
            .asGif()
            .load(gifUrl)
            .into(binding.imgExercicio)

        // BOTÃO DE PRÓXIMO (ainda pode ser configurado depois)
        binding.btnProximo.setOnClickListener {
            // Aqui você define o próximo exercício
        }
    }
}