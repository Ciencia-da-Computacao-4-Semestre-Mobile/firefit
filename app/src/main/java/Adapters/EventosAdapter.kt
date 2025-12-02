package Adapters

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemEventoBinding
import com.example.myapplication.Evento
import com.example.myapplication.TipoEvento

class EventosAdapter(
    private val onClick: (Evento) -> Unit
) : ListAdapter<Evento, EventosAdapter.EventoViewHolder>(EventoDiffCallback()) {

    inner class EventoViewHolder(val binding: ItemEventoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(evento: Evento) {
            binding.txtNomeEvento.text = evento.nome
            binding.txtProfessorEvento.text = evento.professor
            binding.txtDataEvento.text = evento.data.toString()
            binding.txtHoraEvento.text = evento.hora.toString()

            val color = when (evento.tipo) {
                TipoEvento.MUSCULACAO -> binding.root.context.getColor(R.color.holo_green_dark)
                TipoEvento.YOGA -> binding.root.context.getColor(R.color.holo_blue_light)
                TipoEvento.FUNCIONAL -> binding.root.context.getColor(R.color.holo_orange_light)
            }
            binding.cardEvento.setCardBackgroundColor(color)

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

    // Função para filtrar por tipo
    fun filtrarPorTipo(tipo: TipoEvento) {
        submitList(currentList.filter { it.tipo == tipo })
    }
}

class EventoDiffCallback : DiffUtil.ItemCallback<Evento>() {
    override fun areItemsTheSame(oldItem: Evento, newItem: Evento) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Evento, newItem: Evento) = oldItem == newItem
}
