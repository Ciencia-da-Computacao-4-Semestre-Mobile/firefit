package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemEventoBinding
import com.example.myapplication.model.Evento

import com.example.myapplication.R


class EventosAdapter(
    private val lista: List<Evento>
) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemEventoBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = lista[position]
        val ctx = holder.itemView.context

        // ------- TÍTULO -------
        holder.binding.txtTitulo.text = evento.titulo

        // ------- DATA -------
        holder.binding.txtData.text = evento.data

        // ------- HORA -------
        holder.binding.txtHora.text = evento.hora

        // ------- IMAGEM DINÂMICA -------
        holder.binding.imgEvento.setImageResource(evento.imagem)

        // ------- STATUS -------
        holder.binding.txtStatus.text = evento.status.uppercase()

        // muda o fundo automaticamente
        when (evento.status.lowercase()) {

            "confirmado" -> {
                holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_confirmado)
            }

            "em andamento" -> {
                holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_andamento)
            }

            "concluído", "concluido" -> {
                holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_concluido)
            }

            else -> {
                holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_andamento)
            }
        }
    }

    override fun getItemCount() = lista.size
}
