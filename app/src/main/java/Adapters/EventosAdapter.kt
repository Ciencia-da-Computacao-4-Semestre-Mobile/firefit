package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Evento
import com.example.myapplication.R

class EventosAdapter(
    private val context: Context,
    private val lista: List<Evento>,
    private val onClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // usa a propriedade 'status' do modelo
        return lista[position].status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val layoutId = when (viewType) {
            1 -> R.layout.item_evento_disponivel
            2 -> R.layout.item_evento_agendado
            3 -> R.layout.item_evento_concluido
            else -> R.layout.item_evento_disponivel
        }

        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    inner class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtNome = itemView.findViewById<TextView>(R.id.txtNome)
        private val txtProfessor = itemView.findViewById<TextView>(R.id.txtProfessor)
        private val txtDataHora = itemView.findViewById<TextView>(R.id.txtDataHora)
        private val btnAcao = itemView.findViewById<Button?>(R.id.btnAcao)

        fun bind(evento: Evento) {
            txtNome.text = evento.nome
            txtProfessor.text = evento.professor
            txtDataHora.text = "${evento.data} - ${evento.hora}"

            btnAcao?.apply {
                setOnClickListener {
                    onClick(evento)
                }
            }
        }
    }
}
