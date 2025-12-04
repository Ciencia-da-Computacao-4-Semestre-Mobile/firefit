package com.example.myapplication

data class Evento(
    val id: Int,
    val nome: String,
    val professor: String,
    val tipo: TipoEvento,
    val data: String,       // ALTERADO: era LocalDate
    val hora: String,       // ALTERADO: era LocalTime
    val duracao: Int,

    val imagemUrl: String = "",
    var isFavorito: Boolean = false
)
