package com.example.myapplication.model

data class Event(
    val id: Int,
    val title: String,      // título do evento
    val date: String,       // data
    val time: String,       // hora
    val image: Int,         // drawable da imagem
    val status: String,     // "in progress", "completed", etc
    var isScheduled: Boolean = false,  // se foi agendado
    val isFinished: Boolean = false    // finalizado
)