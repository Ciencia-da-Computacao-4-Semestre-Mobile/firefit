package Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
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

    inner class EventoViewHolder(val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(evento: Evento) {

            binding.txtNomeEvento.text = evento.nome
            binding.txtProfessorEvento.text = evento.professor
            binding.txtDataEvento.text = evento.data.toString()
            binding.txtHoraEvento.text = evento.hora.toString()

            val color = when (evento.tipo) {
                TipoEvento.MUSCULACAO -> binding.root.context.getColor(android.R.color.holo_green_dark)
                TipoEvento.YOGA -> binding.root.context.getColor(android.R.color.holo_blue_light)
                TipoEvento.FUNCIONAL -> binding.root.context.getColor(android.R.color.holo_orange_light)
            }
            binding.cardEvento.setCardBackgroundColor(color)

            // --- FOTO DO EVENTO ---
            Glide.with(binding.root.context)
                .load(evento.imagemUrl)
                .placeholder(com.example.myapplication.R.drawable.placeholder_evento)
                .into(binding.imgEvento)

            // --- OVERLAY SUAVE (CORRIGIDO) ---
            try {
                val drawable = ContextCompat.getDrawable(
                    binding.root.context,
                    com.example.myapplication.R.drawable.gradient_red_overlay
                )

                if (binding.imgOverlay is ImageView) {
                    (binding.imgOverlay as ImageView).setImageDrawable(drawable)
                } else {
                    binding.imgOverlay.background = drawable
                }

            } catch (e: Exception) {
                Log.e("EventosAdapter", "Erro no imgOverlay: ${e.message}")
            }

            // --- FAVORITO ---
            val icon = if (evento.isFavorito) {
                com.example.myapplication.R.drawable.ic_favorite_filled
            } else {
                com.example.myapplication.R.drawable.ic_favorite_outline
            }
            binding.btnFavorito.setImageResource(icon)

            binding.btnFavorito.setOnClickListener {
                evento.isFavorito = !evento.isFavorito
                val newIcon = if (evento.isFavorito) {
                    com.example.myapplication.R.drawable.ic_favorite_filled
                } else {
                    com.example.myapplication.R.drawable.ic_favorite_outline
                }
                binding.btnFavorito.setImageResource(newIcon)
            }

            // --- BOTÃO ABRIR ---
            binding.btnAbrir.setOnClickListener { onClick(evento) }
            binding.root.setOnClickListener { onClick(evento) }
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
