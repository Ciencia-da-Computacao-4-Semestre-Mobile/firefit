package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class CardioIntensityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_intensity)

        val tipo = intent.getStringExtra("tipoCardio") ?: "cardio"

        val intensities = listOf("Leve", "Moderado", "Intenso")
        val listView = findViewById<ListView>(R.id.listCardioIntensity)
        val adapter = ArrayAdapter(this, R.layout.list_item_intensity, R.id.textItem, intensities)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, pos, _ ->
            val selected = intensities[pos]
            val i = Intent(this, CardioDurationActivity::class.java)
            i.putExtra("tipoCardio", tipo)
            i.putExtra("intensity", selected)
            startActivity(i)
        }
    }
}
