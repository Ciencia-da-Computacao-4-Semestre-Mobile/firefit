package com.example.myapplication.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.BicicletaAdapter

class BicicletaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BicicletaAdapter
    private val listaBicicleta = listOf(
        "Pedalada Leve - 10 min",
        "Pedalada Moderada - 15 min",
        "Pedalada Intensa - 20 min"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bicicleta, container, false)
        recyclerView = view.findViewById(R.id.recyclerBicicleta)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BicicletaAdapter(listaBicicleta)
        recyclerView.adapter = adapter
        return view
    }
}
