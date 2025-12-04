package com.example.myapplication

import androidx.annotation.DrawableRes

data class Evento(
    val id: Int,
    val nome: String,
    val professor: String,
    val tipo: TipoEvento,
    val data: String,       // dd/MM/yyyy
    val hora: String,       // HH:mm
    val duracao: Int,
    @DrawableRes val imagemResId: Int = R.drawable.placeholder_evento,
    var isFavorito: Boolean = false,
    val imagemUrl: String?
)
