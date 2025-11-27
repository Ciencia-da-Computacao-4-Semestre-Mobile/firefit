package com.example.myapplication.adapters




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CorridaAdapter(private val lista: List<String>) :
    RecyclerView.Adapter<CorridaAdapter.CorridaViewHolder>() {

    class CorridaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCorrida: TextView = itemView.findViewById(R.id.txtCorrida)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorridaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista_corrida, parent, false)
        return CorridaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CorridaViewHolder, position: Int) {
        holder.txtCorrida.text = lista[position]
    }

    override fun getItemCount(): Int = lista.size
}
