package Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Evento
import com.example.myapplication.TipoEvento
import com.example.myapplication.databinding.ItemEventoBinding

class EventosAdapter(
    private val onClick: (Evento) -> Unit
) : ListAdapter<Evento, EventosAdapter.EventoViewHolder>(EventoDiffCallback()) {

    inner class EventoViewHolder(private val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(evento: Evento) {

            // Dados do evento
            binding.txtNomeEvento.text = evento.nome
            binding.txtProfessorEvento.text = evento.professor
            binding.txtDataEvento.text = evento.data
            binding.txtHoraEvento.text = evento.hora

            // Foto do evento
            Glide.with(binding.root.context)
                .load(evento.imagemResId)
                .into(binding.imgEvento)

            // Overlay
            try {
                val overlay = ContextCompat.getDrawable(binding.root.context, com.example.myapplication.R.drawable.gradient_red_overlay)
                binding.imgOverlay.background = overlay
            } catch (e: Exception) {
                Log.e("EventosAdapter", "Erro no overlay: ${e.message}")
            }

            // Favorito
            atualizarIconeFavorito(evento)
            binding.btnFavorito.setOnClickListener {
                evento.isFavorito = !evento.isFavorito
                atualizarIconeFavorito(evento)
            }

            // Cor do card (opcional, se quiser destacar tipo)
            val color = when (evento.tipo) {
                TipoEvento.MUSCULACAO -> ContextCompat.getColor(binding.root.context, android.R.color.holo_green_dark)
                TipoEvento.YOGA -> ContextCompat.getColor(binding.root.context, android.R.color.holo_blue_light)
                TipoEvento.FUNCIONAL -> ContextCompat.getColor(binding.root.context, android.R.color.holo_orange_light)
            }
            binding.cardEvento.setCardBackgroundColor(color)

            // Clique para abrir ConfirmarEventoActivity
            binding.btnAbrir.setOnClickListener { onClick(evento) }
            binding.root.setOnClickListener { onClick(evento) }
        }

        private fun atualizarIconeFavorito(evento: Evento) {
            val icone = if (evento.isFavorito) {
                com.example.myapplication.R.drawable.ic_favorite_filled
            } else {
                com.example.myapplication.R.drawable.ic_favorite_outline
            }
            binding.btnFavorito.setImageResource(icone)
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
