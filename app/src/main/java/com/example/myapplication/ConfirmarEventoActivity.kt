package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityConfirmarEventoBinding
import java.text.SimpleDateFormat
import java.util.*

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val evento = intent.getSerializableExtra("evento") as Evento

        binding.txtTitulo.text = evento.nome
        binding.txtInfo.text = """
            Professor: ${evento.professor}
            Data: ${evento.data}
            Hora: ${evento.hora}
            Duração: ${evento.duracao} min
        """.trimIndent()

        binding.btnConfirmar.setOnClickListener {
            adicionarAoCalendario(evento)
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnAdicionarCalendario.setOnClickListener {
            adicionarAoCalendario(evento)
        }
    }

    private fun adicionarAoCalendario(evento: Evento) {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = sdf.parse("${evento.data} ${evento.hora}") ?: return

        val inicio = date.time
        val fim = inicio + (evento.duracao * 60 * 1000)

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, inicio)
            put(CalendarContract.Events.DTEND, fim)
            put(CalendarContract.Events.TITLE, evento.nome)
            put(CalendarContract.Events.DESCRIPTION, "Aula com ${evento.professor}")
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

        if (uri != null) {
            Toast.makeText(this, "Adicionado ao calendário!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro ao adicionar!", Toast.LENGTH_SHORT).show()
        }
    }
}
