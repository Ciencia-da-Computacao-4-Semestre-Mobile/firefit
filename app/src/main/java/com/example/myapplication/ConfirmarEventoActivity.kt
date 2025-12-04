package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityConfirmarEventoBinding
import com.example.myapplication.databinding.ItemEventoBinding
import java.text.SimpleDateFormat
import java.util.*

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEventoBinding
    private lateinit var cardBinding: ItemEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o binding do include
        cardBinding = ItemEventoBinding.bind(binding.cardEventoConfirmacao)

        // Recebendo dados do Intent
        val nome = intent.getStringExtra("nome") ?: "Evento"
        val professor = intent.getStringExtra("professor") ?: "Desconhecido"
        val tipoStr = intent.getStringExtra("tipo") ?: "MUSCULACAO"
        val duracao = intent.getIntExtra("duracao", 60)
        val data = intent.getStringExtra("data") ?: "01/01/2025"
        val hora = intent.getStringExtra("hora") ?: "00:00"
        val imagemResId = intent.getIntExtra("imagemResId", R.drawable.placeholder_evento)

        // Converter string para TipoEvento
        val tipo = try {
            TipoEvento.valueOf(tipoStr)
        } catch (e: IllegalArgumentException) {
            TipoEvento.MUSCULACAO
        }

        // --- Atualiza UI do card ---
        cardBinding.txtNomeEvento.text = nome
        cardBinding.txtProfessorEvento.text = "Com: $professor"
        cardBinding.txtDataEvento.text = data
        cardBinding.txtHoraEvento.text = hora

        // --- Carrega imagem do card ---
        Glide.with(this)
            .load(imagemResId)
            .placeholder(R.drawable.placeholder_evento)
            .into(cardBinding.imgEvento)

        // --- Atualiza cor do card (opcional, pode deixar preto ou colorido por tipo) ---
        val color = when (tipo) {
            TipoEvento.MUSCULACAO -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
            TipoEvento.YOGA -> ContextCompat.getColor(this, android.R.color.holo_blue_light)
            TipoEvento.FUNCIONAL -> ContextCompat.getColor(this, android.R.color.holo_orange_light)
        }
        cardBinding.cardEvento.setCardBackgroundColor(color)

        // --- Favorito ---
        var isFavorito = intent.getBooleanExtra("isFavorito", false)
        atualizarIconeFavorito(isFavorito)
        cardBinding.btnFavorito.setOnClickListener {
            isFavorito = !isFavorito
            atualizarIconeFavorito(isFavorito)
        }

        // --- Botões ---
        binding.btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Evento confirmado!", Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelar.setOnClickListener { finish() }

        binding.btnAdicionarCalendario.setOnClickListener {
            adicionarAoCalendario(nome, data, hora, duracao)
        }
    }

    private fun atualizarIconeFavorito(isFavorito: Boolean) {
        val icone = if (isFavorito)
            R.drawable.ic_favorite_filled
        else
            R.drawable.ic_favorite_outline
        cardBinding.btnFavorito.setImageResource(icone)
    }

    private fun adicionarAoCalendario(nome: String, data: String, hora: String, duracao: Int) {
        val partesData = data.split("/")
        val partesHora = hora.split(":")

        if (partesData.size < 3 || partesHora.size < 2) {
            Toast.makeText(this, "Data ou hora inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val dia = partesData[0].toIntOrNull() ?: 1
        val mes = (partesData[1].toIntOrNull() ?: 1) - 1
        val ano = partesData[2].toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        val horaInt = partesHora[0].toIntOrNull() ?: 0
        val minutoInt = partesHora[1].toIntOrNull() ?: 0

        val calendar = Calendar.getInstance().apply {
            set(ano, mes, dia, horaInt, minutoInt)
        }

        val startMillis = calendar.timeInMillis
        val endMillis = startMillis + duracao * 60 * 1000L

        val intent = Intent(Intent.ACTION_INSERT).apply {
            setData(CalendarContract.Events.CONTENT_URI)
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
