package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class CardioDurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_duration)

        val tipo = intent.getStringExtra("tipoCardio") ?: "cardio"
        val intensity = intent.getStringExtra("intensity") ?: "Moderado"

        // Lista completa com minutos + horas
        val durations = listOf(
            "10 minutos", "15 minutos", "20 minutos", "25 minutos", "30 minutos",
            "35 minutos", "40 minutos", "45 minutos", "50 minutos", "55 minutos",

            "1 hora",
            "1h10", "1h20", "1h30", "1h40", "1h50",

            "2 horas"
        )

        val listView = findViewById<ListView>(R.id.listCardioDuration)

        // Adaptador correto para usar seu layout customizado
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item_duration,   // layout do item da lista
            R.id.textItem,                 // ID do TextView dentro do layout
            durations
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, pos, _ ->
            val selectedDuration = durations[pos]

            val i = Intent(this, CardioStartActivity::class.java).apply {
                putExtra("tipoCardio", tipo)
                putExtra("intensity", intensity)
                putExtra("duration", selectedDuration)
            }

            startActivity(i)
        }
    }
}
