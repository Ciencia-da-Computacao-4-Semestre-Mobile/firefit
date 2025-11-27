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

        // você pode customizar as opções por tipo, aqui mantemos 3 níveis
        val intensities = listOf("Leve", "Moderado", "Intenso")
        val listView = findViewById<ListView>(R.id.listCardioIntensity)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, intensities)

        listView.setOnItemClickListener { _, _, pos, _ ->
            val selected = intensities[pos]
            val i = Intent(this, CardioDurationActivity::class.java)
            i.putExtra("tipoCardio", tipo)
            i.putExtra("intensity", selected)
            startActivity(i)
        }
    }
}
