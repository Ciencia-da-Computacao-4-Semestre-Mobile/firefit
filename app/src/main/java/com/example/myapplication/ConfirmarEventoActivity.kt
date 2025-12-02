package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityConfirmarEventoBinding
import java.util.Calendar

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebendo dados do Intent
        val nome = intent.getStringExtra("nome") ?: "Evento"
        val professor = intent.getStringExtra("professor") ?: "Desconhecido"
        val tipoStr = intent.getStringExtra("tipo") ?: "MUSCULACAO"
        val duracao = intent.getIntExtra("duracao", 60)
        val data = intent.getStringExtra("data") ?: "01/01/2025"   // dd/MM/yyyy
        val hora = intent.getStringExtra("hora") ?: "00:00"         // HH:mm

        // Converter string para TipoEvento
        val tipo = try {
            TipoEvento.valueOf(tipoStr)
        } catch (e: IllegalArgumentException) {
            TipoEvento.MUSCULACAO
        }

        // Atualizando UI
        binding.txtTitulo.text = nome
        binding.txtInfo.text = "Professor: $professor\nData: $data\nHora: $hora\nDuração: $duracao min"

        // Cor do card baseada no tipo
        val color = when (tipo) {
            TipoEvento.MUSCULACAO -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
            TipoEvento.YOGA -> ContextCompat.getColor(this, android.R.color.holo_blue_light)
            TipoEvento.FUNCIONAL -> ContextCompat.getColor(this, android.R.color.holo_orange_light)
        }
        binding.cardDetalhe.setCardBackgroundColor(color)

        // Botões
        binding.btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Evento confirmado!", Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelar.setOnClickListener { finish() }

        binding.btnAdicionarCalendario.setOnClickListener {
            adicionarAoCalendario(nome, data, hora, duracao)
        }
    }

    private fun adicionarAoCalendario(nome: String, data: String, hora: String, duracao: Int) {
        // Parsing de data e hora
        val partesData = data.split("/")
        val partesHora = hora.split(":")

        if (partesData.size < 3 || partesHora.size < 2) {
            Toast.makeText(this, "Data ou hora inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val dia = partesData[0].toIntOrNull() ?: 1
        val mes = (partesData[1].toIntOrNull() ?: 1) - 1 // Calendar: 0 = Janeiro
        val ano = partesData[2].toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        val horaInt = partesHora[0].toIntOrNull() ?: 0
        val minutoInt = partesHora[1].toIntOrNull() ?: 0

        val calendar = Calendar.getInstance().apply {
            set(ano, mes, dia, horaInt, minutoInt)
        }

        val startMillis = calendar.timeInMillis
        val endMillis = startMillis + duracao * 60 * 1000L

        val intent = Intent(Intent.ACTION_INSERT).apply {
            setData(CalendarContract.Events.CONTENT_URI) // ✅ corrigido
            putExtra(CalendarContract.Events.TITLE, nome)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Não foi possível abrir o calendário", Toast.LENGTH_SHORT).show()
        }
    }
}
