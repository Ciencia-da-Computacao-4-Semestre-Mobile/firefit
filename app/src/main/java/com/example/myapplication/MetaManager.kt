package com.example.myapplication.backend

import android.content.Context

class MetaManager(context: Context) {

    private val prefs = context.getSharedPreferences("metas", Context.MODE_PRIVATE)

    // ==== PESO ====
    fun getPesoAtual(): Int = prefs.getInt("pesoAtual", 85)
    fun getPesoMeta(): Int = prefs.getInt("pesoMeta", 80)

    fun setPesoAtual(valor: Int) {
        prefs.edit().putInt("pesoAtual", valor).apply()
    }

    fun getProgressoPeso(): Int {
        val meta = getPesoMeta()
        val atual = getPesoAtual()

        val progresso = ((meta.toFloat() / atual.toFloat()) * 100)
        return progresso.toInt().coerceIn(0, 100)
    }

    // ==== TREINOS ====
    fun getTreinosMeta(): Int = prefs.getInt("treinosMeta", 5)
    fun getTreinosFeitos(): Int = prefs.getInt("treinosFeitos", 2)

    fun adicionarTreino() {
        val atual = getTreinosFeitos()
        prefs.edit().putInt("treinosFeitos", atual + 1).apply()
    }

    fun getProgressoTreino(): Int {
        val meta = getTreinosMeta()
        val feitos = getTreinosFeitos()

        return ((feitos.toFloat() / meta.toFloat()) * 100)
            .toInt()
            .coerceIn(0, 100)
    }

    // ==== ALIMENTAÇÃO ====
    fun getAlimentacao(): Int = prefs.getInt("alimentacao", 20)

    fun setAlimentacao(valor: Int) {
        prefs.edit().putInt("alimentacao", valor).apply()
    }
}
