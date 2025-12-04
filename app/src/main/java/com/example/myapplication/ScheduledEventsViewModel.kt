package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class ScheduledEventsViewModel : ViewModel() {

    private val _listaEventos = MutableLiveData<List<Evento>>()
    val listaEventos: LiveData<List<Evento>> get() = _listaEventos

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun carregarEventos() {
        _listaEventos.value = listOf(
            Evento(
                1, "Musculação", "Carlos Andrade", TipoEvento.MUSCULACAO,
                "05/12/2025", "14:00", 60,
                imagemResId = R.drawable.crossfit,
                isFavorito = false
            ),
            Evento(
                2, "Yoga", "Larissa Gomes", TipoEvento.YOGA,
                "06/12/2025", "08:00", 60,
                imagemResId = R.drawable.yoga,
                isFavorito = false
            ),
            Evento(
                3, "Funcional", "Pedro Souza", TipoEvento.FUNCIONAL,
                "07/12/2025", "09:30", 45,
                imagemResId = R.drawable.event1,
                isFavorito = false
            )
        )
    }

    fun filtrarEventosFuturos(): List<Evento> {
        val hoje = Calendar.getInstance().time
        return _listaEventos.value?.filter {
            val dataEvento = dateFormat.parse(it.data)
            dataEvento != null && (dataEvento.after(hoje) || dataEvento == hoje)
        } ?: emptyList()
    }

    fun ordenarEventosPorData(): List<Evento> {
        return _listaEventos.value?.sortedWith(compareBy(
            { dateFormat.parse(it.data) ?: Date(0) },
            { timeFormat.parse(it.hora) ?: Date(0) }
        )) ?: emptyList()
    }
}
