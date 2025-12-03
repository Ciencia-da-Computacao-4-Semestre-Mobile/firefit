package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import Adapters.EventosAdapter
import com.example.myapplication.databinding.ActivityScheduledEventsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScheduledEventsActivity : AppCompatActivity() {

    private lateinit var adapter: EventosAdapter
    private val viewModel: ScheduledEventsViewModel by viewModels()
    private lateinit var binding: ActivityScheduledEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduledEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EventosAdapter { evento -> abrirTelaConfirmacao(evento) }

        binding.recyclerScheduled.layoutManager = LinearLayoutManager(this)
        binding.recyclerScheduled.adapter = adapter

        viewModel.listaEventos.observe(this) { eventos ->
            adapter.submitList(eventos)
        }

        viewModel.carregarEventos()

    }

    private fun abrirTelaConfirmacao(evento: Evento) {
        val intent = Intent(this, ConfirmarEventoActivity::class.java).apply {
            putExtra("id", evento.id)
            putExtra("nome", evento.nome)
            putExtra("professor", evento.professor)
            putExtra("tipo", evento.tipo.name)
            putExtra("data", evento.data.toString())
            putExtra("hora", evento.hora.toString())
            putExtra("duracao", evento.duracao)
        }
        startActivity(intent)
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_events

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_training -> {
                    startActivity(Intent(this, WorkoutsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_events -> true
                R.id.nav_user -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
