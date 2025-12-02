package com.example.myapplication

import com.example.myapplication.TipoEvento

data class Evento(
    val id: Int,
    val nome: String,
    val professor: String,
    val tipo: TipoEvento,
    val data: String,   // "dd/MM/yyyy"
    val hora: String,   // "HH:mm"
    val duracao: Int    // em minutos
)
