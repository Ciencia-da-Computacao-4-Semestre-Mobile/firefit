package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardioAdapter(
    private val exercicios: List<String>,
    private val layoutRes: Int
) : RecyclerView.Adapter<CardioAdapter.CardioViewHolder>() {

    class CardioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtExercicio: TextView = itemView.findViewById(R.id.txtExercicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return CardioViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardioViewHolder, position: Int) {
        holder.txtExercicio.text = exercicios[position]
    }

    override fun getItemCount(): Int = exercicios.size
}
