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

        val durations = listOf("10 minutos", "20 minutos", "30 minutos", "45 minutos", "60 minutos")
        val listView = findViewById<ListView>(R.id.listCardioDuration)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, durations)

        listView.setOnItemClickListener { _, _, pos, _ ->
            val selectedDuration = durations[pos]
            val i = Intent(this, CardioStartActivity::class.java)
            i.putExtra("tipoCardio", tipo)
            i.putExtra("intensity", intensity)
            i.putExtra("duration", selectedDuration)
            startActivity(i)
        }
    }
}
