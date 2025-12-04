package com.example.myapplication

data class Evento(
    val id: Int,
    val nome: String,
    val professor: String,
    val tipo: TipoEvento,
    val data: String,
    val hora: String,
    val duracao: Int,
    val imagemResId: Int = R.drawable.crossfit, // default drawable
    var isFavorito: Boolean = false
)

