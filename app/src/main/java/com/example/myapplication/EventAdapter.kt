package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemEventBinding
import com.example.myapplication.model.Event

class EventAdapter(
    private val events: List<Event>,
    private val onSchedule: (Event) -> Unit,
    private val onCancel: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        with(holder.binding) {
            txtTitle.text = event.title
            txtDate.text = event.date
            txtTime.text = event.time
            txtStatus.text = event.status.uppercase()
            imgEvent.setImageResource(event.image)

            // Status background
            txtStatus.setBackgroundResource(
                when (event.status.lowercase()) {
                    "completed", "concluído", "concluido" -> R.drawable.bg_status_concluido
                    "in progress", "em andamento" -> R.drawable.bg_status_andamento
                    "confirmed", "confirmado" -> R.drawable.bg_status_confirmado
                    else -> R.drawable.bg_status_andamento
                }
            )

            // Botão agendar
            btnSchedule.isEnabled = !event.isFinished
            btnSchedule.setOnClickListener { onSchedule(event) }

            // Botão cancelar
            btnCancel.setOnClickListener { onCancel(event) }
        }
    }

    override fun getItemCount() = events.size
}
