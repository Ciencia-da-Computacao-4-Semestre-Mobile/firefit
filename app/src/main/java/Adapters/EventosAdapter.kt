package Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.ConfirmarEventoActivity
import com.example.myapplication.Evento
import com.example.myapplication.TipoEvento
import com.example.myapplication.databinding.ItemEventoBinding

class EventosAdapter(
    private val onClick: (Evento) -> Unit
) : ListAdapter<Evento, EventosAdapter.EventoViewHolder>(EventoDiffCallback()) {

    inner class EventoViewHolder(private val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(evento: Evento) {

            // --- Dados do evento ---
            binding.txtNomeEvento.text = evento.nome
            binding.txtProfessorEvento.text = evento.professor
            binding.txtDataEvento.text = evento.data
            binding.txtHoraEvento.text = evento.hora

            // --- Cor do card por categoria ---
            val color = when (evento.tipo) {
                TipoEvento.MUSCULACAO -> ContextCompat.getColor(binding.root.context, android.R.color.holo_green_dark)
                TipoEvento.YOGA -> ContextCompat.getColor(binding.root.context, android.R.color.holo_blue_light)
                TipoEvento.FUNCIONAL -> ContextCompat.getColor(binding.root.context, android.R.color.holo_orange_light)
            }
            binding.cardEvento.setCardBackgroundColor(color)

            // --- Imagem do evento (apenas drawables locais fixos) ---
            val drawableRes = when (evento.imagemUrl?.lowercase()) {
                "event1.webp" -> com.example.myapplication.R.drawable.event1
                "yoga.webp" -> com.example.myapplication.R.drawable.yoga
                "crossfit.webp" -> com.example.myapplication.R.drawable.crossfit
                else -> com.example.myapplication.R.drawable.placeholder_evento
            }

            Glide.with(binding.root.context)
                .load(drawableRes)
                .into(binding.imgEvento)

            // --- Overlay seguro ---
            try {
                val overlay = ContextCompat.getDrawable(binding.root.context, com.example.myapplication.R.drawable.gradient_red_overlay)
                binding.imgOverlay.background = overlay
            } catch (e: Exception) {
                Log.e("EventosAdapter", "Erro no overlay: ${e.message}")
            }

            // --- Favorito ---
            atualizarIconeFavorito(evento)
            binding.btnFavorito.setOnClickListener {
                evento.isFavorito = !evento.isFavorito
                atualizarIconeFavorito(evento)
            }

            // --- Clique para abrir ConfirmarEventoActivity ---
            binding.btnAbrir.setOnClickListener { abrirConfirmarEvento(binding.root.context, evento) }
            binding.root.setOnClickListener { abrirConfirmarEvento(binding.root.context, evento) }
        }

        private fun atualizarIconeFavorito(evento: Evento) {
            val icone = if (evento.isFavorito) {
                com.example.myapplication.R.drawable.ic_favorite_filled
            } else {
                com.example.myapplication.R.drawable.ic_favorite_outline
            }
            binding.btnFavorito.setImageResource(icone)
        }

        private fun abrirConfirmarEvento(context: Context, evento: Evento) {
            val intent = Intent(context, ConfirmarEventoActivity::class.java).apply {
                putExtra("id", evento.id)
                putExtra("nome", evento.nome)
                putExtra("professor", evento.professor)
                putExtra("tipo", evento.tipo.name)
                putExtra("data", evento.data)
                putExtra("hora", evento.hora)
                putExtra("duracao", evento.duracao)
                // <- seguro: nunca passa null, sempre tem placeholder
                putExtra("imagemUrl", evento.imagemUrl ?: "placeholder_evento")
                putExtra("isFavorito", evento.isFavorito)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class EventoDiffCallback : DiffUtil.ItemCallback<Evento>() {
    override fun areItemsTheSame(oldItem: Evento, newItem: Evento) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Evento, newItem: Evento) = oldItem == newItem
}
