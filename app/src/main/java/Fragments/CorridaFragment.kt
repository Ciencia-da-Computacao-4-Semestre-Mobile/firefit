package com.example.myapplication.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.CorridaAdapter

class CorridaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CorridaAdapter
    private val listaCorrida = listOf(
        "Aquecimento - Corrida Leve - 5 min",
        "Corrida Moderada - 10 min",
        "Sprints - 5x30 seg",
        "Corrida de Recuperação - 3 min"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_corrida, container, false)
        recyclerView = view.findViewById(R.id.recyclerCorrida)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CorridaAdapter(listaCorrida)
        recyclerView.adapter = adapter
        return view
    }
}
