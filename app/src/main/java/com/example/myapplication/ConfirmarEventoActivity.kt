package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.*

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var txtNome: TextView
    private lateinit var txtProfessor: TextView
    private lateinit var txtData: TextView
    private lateinit var txtHora: TextView
    private lateinit var txtDuracao: TextView
    private lateinit var imgEvento: ImageView
    private lateinit var btnConfirmar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private lateinit var btnAdicionarCalendario: MaterialButton
    private lateinit var btnBack: ImageView   // <- botão de sair/voltar

    // Variável para controlar se o evento está agendado
    private var eventoAgendado = false

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
        btnConfirmar = findViewById(R.id.btnConfirmar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnAdicionarCalendario = findViewById(R.id.btnAdicionarCalendario)
        btnBack = findViewById(R.id.btnBack)  // <- inicializando

        // Botão de voltar/sair
        btnBack.setOnClickListener {
            finish()  // Fecha a activity atual
        }

        // Recebendo dados do Intent
        val nome = intent.getStringExtra("nome") ?: "Evento"
        val professor = intent.getStringExtra("professor") ?: "-"
        val data = intent.getStringExtra("data") ?: "-"
        val hora = intent.getStringExtra("hora") ?: "-"
        val duracao = intent.getStringExtra("duracao") ?: "-"
        val imagemUrl = intent.getStringExtra("imagemUrl") ?: "placeholder_evento"

        // Setando textos
        txtNome.text = nome
        txtProfessor.text = "Professor: $professor"
        txtData.text = "Data: $data"
        txtHora.text = "Hora: $hora"
        txtDuracao.text = "Duração: $duracao"

        // Setando imagem do evento de forma segura
        val drawableRes = when (imagemUrl.lowercase()) {
            "event1.webp" -> R.drawable.event1
            "yoga.webp" -> R.drawable.yoga
            "crossfit.webp" -> R.drawable.crossfit
            else -> R.drawable.placeholder_evento
        }
        imgEvento.setImageResource(drawableRes)

        // Lógica do botão SALVAR
        btnConfirmar.setOnClickListener {
            if (!eventoAgendado) {
                eventoAgendado = true
                Toast.makeText(this, "Evento agendado!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Evento já agendado.", Toast.LENGTH_SHORT).show()
            }
        }

        // Lógica do botão CANCELAR
        btnCancelar.setOnClickListener {
            if (eventoAgendado) {
                eventoAgendado = false
                Toast.makeText(this, "Agendamento cancelado.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nenhum evento agendado.", Toast.LENGTH_SHORT).show()
            }
        }

        // Lógica do botão ADICIONAR AO CALENDÁRIO
        btnAdicionarCalendario.setOnClickListener {
            if (!eventoAgendado) {
                Toast.makeText(this, "Agende o evento primeiro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val calendar = Calendar.getInstance()
                val dataParts = txtData.text.toString().replace("Data: ", "").split("/")
                val horaParts = txtHora.text.toString().replace("Hora: ", "").split(":")

                calendar.set(
                    dataParts[2].toInt(),      // ano
                    dataParts[1].toInt() - 1,  // mês (0-based)
                    dataParts[0].toInt(),      // dia
                    horaParts[0].toInt(),      // hora
                    horaParts[1].toInt()       // minuto
                )
                val startMillis = calendar.timeInMillis
                val duracaoMin = txtDuracao.text.toString().replace("Duração: ", "").toLong()
                val endMillis = startMillis + duracaoMin * 60 * 1000

                // Criando a intent corretamente com Uri
                val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                    putExtra(CalendarContract.Events.TITLE, txtNome.text.toString())
                    putExtra(CalendarContract.Events.DESCRIPTION, txtProfessor.text.toString())
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Nenhum aplicativo de calendário encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao adicionar ao calendário.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
