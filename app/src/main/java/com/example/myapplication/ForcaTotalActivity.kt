package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ForcaTotalActivity : AppCompatActivity() {

    private lateinit var recyclerExercicios: RecyclerView
    private lateinit var groupCardio: RadioGroup
    private lateinit var btnIniciar: Button
    private lateinit var btnVoltar: Button
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forca_total)

        // ========= INICIALIZAR VIEWS =========
        recyclerExercicios = findViewById(R.id.recyclerExercicios)
        groupCardio = findViewById(R.id.groupCardio)
        btnIniciar = findViewById(R.id.btnIniciarTreino)
        btnVoltar = findViewById(R.id.btnVoltar)
        bottomNav = findViewById(R.id.bottomNav)

        // ========= LISTA PADRÃO =========
        carregarLista(R.layout.item_lista_corrida)
        groupCardio.check(R.id.rbCorrida)

        // ========= TROCA DE LISTA COM RADIO =========
        groupCardio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCorrida -> carregarLista(R.layout.item_lista_corrida)
                R.id.rbBicicleta -> carregarLista(R.layout.item_lista_bicicleta)
                R.id.rbCaminhada -> carregarLista(R.layout.item_lista_caminhada)
            }
        }


        // ========= BOTÃO INICIAR =========
        btnIniciar.setOnClickListener {
            val selectedId = groupCardio.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Selecione um tipo de cardio!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipo = when (selectedId) {
                R.id.rbCorrida -> "Superior + Musculação"
                R.id.rbBicicleta -> "Superior"
                R.id.rbCaminhada -> "Inferior"
                else -> "corrida"
            }

            val intent = Intent(this, CardioIntensityActivity::class.java)
            intent.putExtra("tipoCardio", tipo)
            startActivity(intent)
        }

        // ========= BOTÃO VOLTAR =========
        btnVoltar.setOnClickListener { finish() }

        // ========= BOTTOM NAV =========
        bottomNav.selectedItemId = R.id.nav_training
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navegarPara(HomeActivity::class.java)
                R.id.nav_training -> true // já está na mesma tela
                R.id.nav_user -> navegarPara(UserActivity::class.java)
                R.id.nav_events -> navegarPara(ScheduledEventsActivity::class.java)
                else -> false
            }
        }
    }

    // ========= FUNÇÃO PARA CARREGAR LISTA =========
    private fun carregarLista(layoutRes: Int) {
        val listaExercicios = when (layoutRes) {
            R.layout.item_lista_corrida -> listOf("Burpee Completo", "Agachamento com Desenvolvimento", "Prancha com Toque no Ombro")
            R.layout.item_lista_bicicleta -> listOf("Flexão de braço", "Remada com elástico ou mochila", "Elevação lateral")
            R.layout.item_lista_caminhada -> listOf("Agachamento", "Afundo (Lunge)", "Elevação de panturrilha")
            else -> emptyList()
        }

        val adapter = CardioAdapter(listaExercicios, layoutRes)
        recyclerExercicios.layoutManager = LinearLayoutManager(this)
        recyclerExercicios.adapter = adapter
    }

    // ========= FUNÇÃO AUXILIAR DE NAVEGAÇÃO =========
    private fun navegarPara(activityClass: Class<*>): Boolean {
        if (this::class.java != activityClass) {
            startActivity(Intent(this, activityClass))
            overridePendingTransition(0, 0)
            finish()
        }
        return true
    }
}
