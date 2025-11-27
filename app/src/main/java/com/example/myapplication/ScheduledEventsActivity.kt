package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScheduledEventsActivity : AppCompatActivity() {

    // Lista de eventos
    private val events = arrayListOf(
        EventModel("Yoga", "Pedro Marcal", "02/12", "20:35", "Disponível"),
        EventModel("Pilates", "Maria Silva", "03/12", "18:00", "Concluído"),
        EventModel("HIIT", "João Souza", "04/12", "07:30", "Disponível")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_events)

        val listProximosEventos = findViewById<LinearLayout>(R.id.listProximosEventos)
        val listEventosFinalizados = findViewById<LinearLayout>(R.id.listEventosFinalizados)

        // Limpa antes de adicionar
        listProximosEventos.removeAllViews()
        listEventosFinalizados.removeAllViews()

        val inflater = LayoutInflater.from(this)

        events.forEach { event ->
            val itemView = inflater.inflate(R.layout.item_events, listProximosEventos, false)

            // Preenche os dados do card
            itemView.findViewById<TextView>(R.id.tvTitle).text = event.title
            itemView.findViewById<TextView>(R.id.tvInstructor).text = event.instructor
            itemView.findViewById<TextView>(R.id.tvDate).text = "Data: ${event.date}"
            itemView.findViewById<TextView>(R.id.tvTime).text = "Horário: ${event.time}"
            itemView.findViewById<TextView>(R.id.tvStatus).text = event.status.uppercase()

            // Ajusta background do status
            val statusView = itemView.findViewById<TextView>(R.id.tvStatus)
            when (event.status.lowercase()) {
                "disponível" -> statusView.setBackgroundResource(R.drawable.bg_status_disponivel)
                "agendado" -> statusView.setBackgroundResource(R.drawable.bg_status_agendado)
                "concluído" -> statusView.setBackgroundResource(R.drawable.bg_status_concluido)
            }

            // Se o evento já estiver concluído, vai para a lista de finalizados
            if (event.status.lowercase() == "concluído") {
                listEventosFinalizados.addView(itemView)
            } else {
                listProximosEventos.addView(itemView)

                // Se clicar no card, marca como agendado
                itemView.setOnClickListener {
                    event.status = "Agendado"
                    statusView.text = "AGENDADO"
                    statusView.setBackgroundResource(R.drawable.bg_status_agendado)
                }
            }
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_events

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    if (this !is HomeActivity) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_training -> {
                    if (this !is WorkoutsActivity) {
                        startActivity(Intent(this, WorkoutsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_user -> {
                    if (this !is UserActivity) {
                        startActivity(Intent(this, UserActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_events -> {
                    if (this !is ScheduledEventsActivity) {
                        startActivity(Intent(this, ScheduledEventsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                else -> false
            }
        }


    }
}
