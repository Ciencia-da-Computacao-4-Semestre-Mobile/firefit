package com.example.myapplication.utils

import android.content.Context

object FavoriteManager {

    private const val PREF_NAME = "FAVORITOS"
    private const val KEY_FAVORITOS = "favoritos_set"

    fun isFavorito(context: Context, id: Int): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val favoritos = prefs.getStringSet(KEY_FAVORITOS, emptySet()) ?: emptySet()
        return favoritos.contains(id.toString())
    }

    fun adicionarFavorito(context: Context, id: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_FAVORITOS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        set.add(id.toString())
        prefs.edit().putStringSet(KEY_FAVORITOS, set).apply()
    }

    fun removerFavorito(context: Context, id: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_FAVORITOS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        set.remove(id.toString())
        prefs.edit().putStringSet(KEY_FAVORITOS, set).apply()
    }

    fun toggleFavorito(context: Context, id: Int): Boolean {
        return if (isFavorito(context, id)) {
            removerFavorito(context, id)
            false
        } else {
            adicionarFavorito(context, id)
            true
        }
    }
}
