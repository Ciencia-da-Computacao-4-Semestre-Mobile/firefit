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
                1,
                "Musculação",
                "Carlos Andrade",
                TipoEvento.MUSCULACAO,
                "05/12/2025",
                "14:00",
                60,
                "https://i.imgur.com/0Z8FjQp.jpeg"
            ),
            Evento(
                2,
                "Yoga",
                "Larissa Gomes",
                TipoEvento.YOGA,
                "06/12/2025",
                "08:00",
                60,
                "https://i.imgur.com/fFeQWkR.jpeg"
            ),
            Evento(
                3,
                "Funcional",
                "Pedro Souza",
                TipoEvento.FUNCIONAL,
                "07/12/2025",
                "09:30",
                45,
                "https://i.imgur.com/pIm2itA.jpeg"
            )
        )
    }

    fun filtrarEventosFuturos(): List<Evento> {
        val hoje = Calendar.getInstance().time
        return _listaEventos.value?.filter {
            val dataConvertida = dateFormat.parse(it.data)
            dataConvertida != null && (dataConvertida.after(hoje) || dataConvertida == hoje)
        } ?: emptyList()
    }

    fun ordenarEventosPorData(): List<Evento> {
        return _listaEventos.value?.sortedWith(compareBy(
            { dateFormat.parse(it.data) ?: Date(0) },
            { timeFormat.parse(it.hora) ?: Date(0) }
        )) ?: emptyList()
    }
}
