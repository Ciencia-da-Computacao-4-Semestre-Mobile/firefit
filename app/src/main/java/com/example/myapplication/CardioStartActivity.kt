package com.example.myapplication

import android.animation.ObjectAnimator
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CardioStartActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null
    private var isRunning = false

    private lateinit var tvTimer: TextView
    private lateinit var btnStartPause: Button
    private lateinit var btnReset: Button
    private lateinit var tvCurrentPhase: TextView

    private var pulseAnimator: ObjectAnimator? = null

    // 🔥 LISTA DE FASES DO TREINO
    private val phases = mutableListOf<Phase>()
    private var currentPhaseIndex = 0
    private var timeLeftMillis: Long = 0L

    data class Phase(
        val name: String,
        val durationMillis: Long
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_start)

        val tipo = intent.getStringExtra("tipoCardio") ?: "Cardio"
        val intensity = intent.getStringExtra("intensity") ?: "Moderado"
        val durationStr = intent.getStringExtra("duration") ?: "30 minutos"

        findViewById<TextView>(R.id.txtCardioTitle).text = "$tipo — $intensity"
        findViewById<TextView>(R.id.txtCardioInfo).text = "Duração: $durationStr"

        tvTimer = findViewById(R.id.tvTimer)
        btnStartPause = findViewById(R.id.btnStartPause)
        btnReset = findViewById(R.id.btnReset)

        // 🔥 NOVO: Texto da fase atual
        tvCurrentPhase = TextView(this).apply {
            textSize = 20f
            setTextColor(resources.getColor(R.color.fire_text, null))
            text = "Aquecimento"
        }
        findViewById<LinearLayout>(R.id.exerciseList).addView(tvCurrentPhase, 0)

        // animação pulse
        pulseAnimator = ObjectAnimator.ofFloat(tvTimer, View.SCALE_X, 1f, 1.08f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        pulseAnimator?.addUpdateListener { tvTimer.scaleY = tvTimer.scaleX }

        // 🔥 MONTA LISTA DE FASES DE ACORDO COM O TIPO
        setupPhases(tipo)

        // 🔥 COMEÇA NA FASE 0
        currentPhaseIndex = 0
        timeLeftMillis = phases[0].durationMillis
        updateTimerText()
        updatePhaseText()

        btnStartPause.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        btnReset.setOnClickListener {
            resetWorkout()
        }
    }

    // 🔥 CONFIGURA AS FASES DO TREINO
    private fun setupPhases(tipo: String) {
        phases.clear()

        when (tipo.lowercase()) {

            "corrida" -> {
                phases += Phase("Aquecimento — Corrida leve", 5 * 60_000)
                phases += Phase("Corrida moderada 1/3", 5 * 60_000)
                phases += Phase("Corrida moderada 2/3", 5 * 60_000)
                phases += Phase("Corrida moderada 3/3", 5 * 60_000)
                phases += Phase("Sprints 1/5", 30_000)
                phases += Phase("Sprints 2/5", 30_000)
                phases += Phase("Sprints 3/5", 30_000)
                phases += Phase("Sprints 4/5", 30_000)
                phases += Phase("Sprints 5/5", 30_000)
                phases += Phase("Corrida leve — Recuperação", 3 * 60_000)
                phases += Phase("Alongamento final", 5 * 60_000)
            }

            "bicicleta" -> {
                phases += Phase("Aquecimento — Leve", 5 * 60_000)
                phases += Phase("Subida 1/4", 4 * 60_000)
                phases += Phase("Subida 2/4", 4 * 60_000)
                phases += Phase("Subida 3/4", 4 * 60_000)
                phases += Phase("Subida 4/4", 4 * 60_000)
                phases += Phase("Pedalada forte 1/3", 3 * 60_000)
                phases += Phase("Pedalada forte 2/3", 3 * 60_000)
                phases += Phase("Pedalada forte 3/3", 3 * 60_000)
                phases += Phase("Ritmo leve", 5 * 60_000)
                phases += Phase("Alongamento final", 5 * 60_000)
            }

            "caminhada" -> {
                phases += Phase("Caminhada leve", 7 * 60_000)
                phases += Phase("Caminhada inclinada", 5 * 60_000)
                phases += Phase("Caminhada rápida 1/3", 3 * 60_000)
                phases += Phase("Caminhada rápida 2/3", 3 * 60_000)
                phases += Phase("Caminhada rápida 3/3", 3 * 60_000)
                phases += Phase("Caminhada leve — Recuperação", 5 * 60_000)
                phases += Phase("Alongamento final", 5 * 60_000)
            }

            else -> {
                phases += Phase("Aquecimento", 5 * 60_000)
                phases += Phase("Treino principal", 20 * 60_000)
                phases += Phase("Resfriamento", 5 * 60_000)
            }
        }
    }

    // 🔥 ATUALIZA TEXTO DA FASE ATUAL
    private fun updatePhaseText() {
        tvCurrentPhase.text = "Fase atual: ${phases[currentPhaseIndex].name}"
    }

    // 🔥 CONTADOR
    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(ms: Long) {
                timeLeftMillis = ms
                updateTimerText()
            }

            override fun onFinish() {
                nextPhase()
            }
        }.start()

        isRunning = true
        updateButtons()
        pulseAnimator?.start()
    }

    private fun nextPhase() {
        currentPhaseIndex++

        if (currentPhaseIndex >= phases.size) {
            finishWorkout()
            return
        }

        playBeep()

        timeLeftMillis = phases[currentPhaseIndex].durationMillis
        updatePhaseText()
        updateTimerText()
        startTimer()
    }

    private fun finishWorkout() {
        isRunning = false
        pulseAnimator?.cancel()
        updateButtons()
        tvTimer.text = "00:00"
        findViewById<TextView>(R.id.txtFinished).visibility = View.VISIBLE
        playBeep()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        updateButtons()
        pulseAnimator?.cancel()
    }

    private fun resetWorkout() {
        pauseTimer()
        currentPhaseIndex = 0
        timeLeftMillis = phases[0].durationMillis
        updatePhaseText()
        updateTimerText()
        findViewById<TextView>(R.id.txtFinished).visibility = View.GONE
    }

    private fun playBeep() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ring = RingtoneManager.getRingtone(applicationContext, notification)
            ring.play()
        } catch (_: Exception) { }
    }

    private fun updateButtons() {
        btnStartPause.text = if (isRunning) "Pausar" else "Iniciar"
    }

    private fun updateTimerText() {
        val sec = timeLeftMillis / 1000
        val min = sec / 60
        val s = sec % 60
        tvTimer.text = String.format("%02d:%02d", min, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        pulseAnimator?.cancel()
    }
}
