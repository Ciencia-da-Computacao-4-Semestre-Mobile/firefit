package com.example.myapplication.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class BicicletaAdapter(private val lista: List<String>) :
    RecyclerView.Adapter<BicicletaAdapter.BicicletaViewHolder>() {

    class BicicletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBicicleta: TextView = itemView.findViewById(R.id.txtBicicleta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BicicletaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista_bicicleta, parent, false)
        return BicicletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BicicletaViewHolder, position: Int) {
        holder.txtBicicleta.text = lista[position]
    }

    override fun getItemCount(): Int = lista.size
}
