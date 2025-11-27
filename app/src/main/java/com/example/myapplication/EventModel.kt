package com.example.myapplication

data class EventModel(
    val title: String,
    val instructor: String,
    val date: String,
    val time: String,
    var status: String, // "Disponível", "Concluído", "Agendado"
    val imageResId: Int = R.drawable.event_placeholder // drawable default
)
