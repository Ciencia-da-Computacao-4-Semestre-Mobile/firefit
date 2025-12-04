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
<<<<<<< HEAD
)
=======
)
>>>>>>> 7a5e6d83e20cc276b0816de2450dc852309083df
