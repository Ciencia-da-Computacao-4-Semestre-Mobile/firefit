package com.example.myapplication

data class Evento(
    val id: Int,
    val nome: String,
    val professor: String,
    val tipo: TipoEvento,
    val data: String,       // dd/MM/yyyy
    val hora: String,       // HH:mm
    val duracao: Int,
    val imagemResId: Int = R.drawable.placeholder_evento, // drawable do evento
    var isFavorito: Boolean = false
)
