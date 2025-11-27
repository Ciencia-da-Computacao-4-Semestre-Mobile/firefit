package com.example.myapplication.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.CaminhadaAdapter

class CaminhadaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CaminhadaAdapter
    private val listaCaminhada = listOf(
        "Caminhada Leve - 10 min",
        "Caminhada Moderada - 15 min",
        "Caminhada Intensa - 20 min"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_caminhada, container, false)
        recyclerView = view.findViewById(R.id.recyclerCaminhada)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CaminhadaAdapter(listaCaminhada)
        recyclerView.adapter = adapter
        return view
    }
}
