package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.EventosAdapter
import com.example.myapplication.Evento

class ScheduledEventsActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: EventosAdapter
    private lateinit var listaEventos: MutableList<Evento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_events)

        recycler = findViewById(R.id.recyclerScheduled)
        recycler.layoutManager = LinearLayoutManager(this)

        listaEventos = gerarEventosAgendados()

        adapter = EventosAdapter(
            context = this,
            lista = listaEventos,
            onClick = { evento ->
                abrirTelaConfirmacao(evento)
            }
        )

        recycler.adapter = adapter
    }

    private fun abrirTelaConfirmacao(evento: Evento) {
        val intent = Intent(this, ConfirmarEventoActivity::class.java)
        intent.putExtra("nome", evento.nome)
        intent.putExtra("professor", evento.professor)
        intent.putExtra("data", evento.data)
        intent.putExtra("hora", evento.hora)
        startActivity(intent)
    }

    private fun gerarEventosAgendados(): MutableList<Evento> {
        return mutableListOf(
            Evento("Musculação", "Carlos Andrade", "12/03", "14:00", 2),
            Evento("Yoga", "Larissa Gomes", "15/03", "08:00", 2),
            Evento("Funcional", "Pedro Souza", "20/03", "09:30", 2)
        )
    }
}
