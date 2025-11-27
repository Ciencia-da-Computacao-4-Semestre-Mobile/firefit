
package com.example.myapplication.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class CaminhadaAdapter(private val lista: List<String>) :
    RecyclerView.Adapter<CaminhadaAdapter.CaminhadaViewHolder>() {

    class CaminhadaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCaminhada: TextView = itemView.findViewById(R.id.txtCaminhada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaminhadaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista_caminhada, parent, false)
        return CaminhadaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaminhadaViewHolder, position: Int) {
        holder.txtCaminhada.text = lista[position]
    }

    override fun getItemCount(): Int = lista.size
}
