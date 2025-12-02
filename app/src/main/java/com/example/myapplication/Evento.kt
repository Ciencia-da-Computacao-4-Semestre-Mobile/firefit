package com.example.myapplication

data class Evento(
    val nome: String,
    val professor: String,
    val data: String,
    val hora: String,
    val status: Int // 1 = disponível, 2 = agendado, 3 = concluído
)
