package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // coloque o nome do xml que você usa (o que você me enviou). Ex: R.layout.activity_cardio
        setContentView(R.layout.activity_cardio)

        // IDs exatamente como no seu XML
        val group = findViewById<RadioGroup>(R.id.groupCardio)
        val btnIniciar = findViewById<Button>(R.id.btnIniciarTreino)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)
        // dentro da sua Activity principal, no click do INICIAR:
        val intent = Intent(this, CardioIntensityActivity::class.java)
        intent.putExtra("tipoCardio", "corrida") // ou "bicicleta" / "caminhada"
        startActivity(intent)


        btnIniciar.setOnClickListener {
            val selectedId = group.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Selecione um tipo de cardio!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (selectedId) {
                R.id.rbCorrida -> startActivity(Intent(this, TelaCorridaActivity::class.java))
                R.id.rbBicicleta -> startActivity(Intent(this, TelaBicicletaActivity::class.java))
                R.id.rbCaminhada -> startActivity(Intent(this, TelaCaminhadaActivity::class.java))
                else -> Toast.makeText(this, "Opção inválida", Toast.LENGTH_SHORT).show()
            }
        }

        btnVoltar.setOnClickListener {
            finish()
        }

    }
}
