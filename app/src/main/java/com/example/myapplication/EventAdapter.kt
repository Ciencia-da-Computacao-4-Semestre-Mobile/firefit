package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class EventAdapter(
    private val context: Context,
    private val data: ArrayList<EventModel>
) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_events, parent, false)

        val event = data[position]

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvInstructor = view.findViewById<TextView>(R.id.tvInstructor)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val imgEvent = view.findViewById<ImageView>(R.id.imgEvent)

        // 🔒 Evita que o card inteiro seja clicável
        view.isClickable = false
        view.isFocusable = false

        // Preenche dados
        tvTitle.text = event.title
        tvInstructor.text = event.instructor
        tvDate.text = "Data: ${event.date}"
        tvTime.text = "Horário: ${event.time}"
        tvStatus.text = event.status.uppercase()
        imgEvent.setImageResource(event.imageResId)

        // Aplica a cor ao status
        when (event.status.lowercase()) {
            "disponível" -> tvStatus.setBackgroundResource(R.drawable.bg_status_disponivel)
            "agendado" -> tvStatus.setBackgroundResource(R.drawable.bg_status_agendado)
            "concluído" -> tvStatus.setBackgroundResource(R.drawable.bg_status_concluido)
        }

        // 👉 Clique SOMENTE no status
        tvStatus.setOnClickListener {
            if (event.status.lowercase() == "disponível") {
                val intent = Intent(context, EventsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }

        return view
    }
}
