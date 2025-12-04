package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityConfirmarEventoBinding

import java.util.*

class ConfirmarEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebendo dados do Intent
        val evento = Evento(
            id = intent.getIntExtra("id", 0),
            nome = intent.getStringExtra("nome") ?: "Evento",
            professor = intent.getStringExtra("professor") ?: "Desconhecido",
            tipo = TipoEvento.valueOf(intent.getStringExtra("tipo") ?: "MUSCULACAO"),
            data = intent.getStringExtra("data") ?: "01/01/2025",
            hora = intent.getStringExtra("hora") ?: "00:00",
            duracao = intent.getIntExtra("duracao", 60),
            imagemResId = intent.getIntExtra("imagemResId", R.drawable.placeholder_evento),
            isFavorito = intent.getBooleanExtra("isFavorito", false)
        )

        // Preencher card do evento
        binding.cardEventoConfirmacao.txtNomeEvento.text = evento.nome
        binding.cardEventoConfirmacao.txtProfessorEvento.text = evento.professor
        binding.cardEventoConfirmacao.txtDataEvento.text = evento.data
        binding.cardEventoConfirmacao.txtHoraEvento.text = evento.hora

        // Cor do card conforme tipo
        val color = when (evento.tipo) {
            TipoEvento.MUSCULACAO -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
            TipoEvento.YOGA -> ContextCompat.getColor(this, android.R.color.holo_blue_light)
            TipoEvento.FUNCIONAL -> ContextCompat.getColor(this, android.R.color.holo_orange_light)
        }
        binding.cardEventoConfirmacao.cardEvento.setCardBackgroundColor(color)

        // Carregar imagem do drawable
        Glide.with(this)
            .load(evento.imagemResId)
            .into(binding.cardEventoConfirmacao.imgEvento)

        // Overlay
        binding.cardEventoConfirmacao.imgOverlay.background =
            ContextCompat.getDrawable(this, R.drawable.gradient_red_overlay)

        // Favorito
        atualizarIconeFavorito(evento)
        binding.cardEventoConfirmacao.btnFavorito.setOnClickListener {
            evento.isFavorito = !evento.isFavorito
            atualizarIconeFavorito(evento)
        }

        // Botões da Activity
        binding.btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Evento confirmado!", Toast.LENGTH_SHORT).show()
        }
        binding.btnCancelar.setOnClickListener { finish() }
        binding.btnAdicionarCalendario.setOnClickListener { adicionarAoCalendario(evento) }
    }

    private fun atualizarIconeFavorito(evento: Evento) {
        val icone = if (evento.isFavorito) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
        binding.cardEventoConfirmacao.btnFavorito.setImageResource(icone)
    }

    private fun adicionarAoCalendario(evento: Evento) {
        val partesData = evento.data.split("/")
        val partesHora = evento.hora.split(":")
        if (partesData.size < 3 || partesHora.size < 2) return

        val dia = partesData[0].toInt()
        val mes = partesData[1].toInt() - 1
        val ano = partesData[2].toInt()
        val horaInt = partesHora[0].toInt()
        val minutoInt = partesHora[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(ano, mes, dia, horaInt, minutoInt)
        }
        val startMillis = calendar.timeInMillis
        val endMillis = startMillis + evento.duracao * 60 * 1000L

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, evento.nome)
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
