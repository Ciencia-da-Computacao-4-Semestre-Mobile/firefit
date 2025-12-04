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
                id = 1,
                nome = "Musculação",
                professor = "Carlos Andrade",
                tipo = TipoEvento.MUSCULACAO,
                data = "05/12/2025",
                hora = "14:00",
                duracao = 60,
                imagemUrl = "crossfit.webp"
            ),
            Evento(
                id = 2,
                nome = "Yoga",
                professor = "Larissa Gomes",
                tipo = TipoEvento.YOGA,
                data = "06/12/2025",
                hora = "08:00",
                duracao = 60,
                imagemUrl = "yoga.webp"
            ),
            Evento(
                id = 3,
                nome = "Funcional",
                professor = "Pedro Souza",
                tipo = TipoEvento.FUNCIONAL,
                data = "07/12/2025",
                hora = "09:30",
                duracao = 45,
                imagemUrl = "event1.webp"
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
