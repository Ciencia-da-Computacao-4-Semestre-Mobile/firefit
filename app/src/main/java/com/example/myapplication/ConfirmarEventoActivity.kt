package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var txtNome: TextView
    private lateinit var txtProfessor: TextView
    private lateinit var txtData: TextView
    private lateinit var txtHora: TextView
    private lateinit var txtDuracao: TextView
    private lateinit var imgEvento: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_evento)

        // Inicializando views
        txtNome = findViewById(R.id.txtNomeEvento)
        txtProfessor = findViewById(R.id.txtProfessorEvento)
        txtData = findViewById(R.id.txtDataEvento)
        txtHora = findViewById(R.id.txtHoraEvento)
        txtDuracao = findViewById(R.id.txtDuracaoEvento)
        imgEvento = findViewById(R.id.imgEvento)

        // Recebendo dados do Intent
        val nome = intent.getStringExtra("nome") ?: "Evento"
        val professor = intent.getStringExtra("professor") ?: "-"
        val data = intent.getStringExtra("data") ?: "-"
        val hora = intent.getStringExtra("hora") ?: "-"
        val duracao = intent.getStringExtra("duracao") ?: "-"
        val imagemUrl = intent.getStringExtra("imagemUrl") ?: "placeholder_evento"

        // Setando texto
        txtNome.text = nome
        txtProfessor.text = professor
        txtData.text = data
        txtHora.text = hora
        txtDuracao.text = duracao

        // Setando imagem do evento de forma segura
        val drawableRes = when(imagemUrl.lowercase()) {
            "event1.webp" -> R.drawable.event1
            "yoga.webp" -> R.drawable.yoga
            "crossfit.webp" -> R.drawable.crossfit
            else -> R.drawable.placeholder_evento
        }

        imgEvento.setImageResource(drawableRes)
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7a5e6d83e20cc276b0816de2450dc852309083df
