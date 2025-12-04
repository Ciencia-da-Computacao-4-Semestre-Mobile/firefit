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
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide

class CardioStartActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null
    private var isRunning = false

    private lateinit var tvTimer: TextView
    private lateinit var btnStartPause: Button
    private lateinit var btnReset: Button
    private lateinit var tvCurrentPhase: TextView
    private lateinit var gifImage: ImageView

    private var pulseAnimator: ObjectAnimator? = null

    // 🔥 LISTA DE FASES DO TREINO
    private val phases = mutableListOf<Phase>()
    private var currentPhaseIndex = 0
    private var timeLeftMillis: Long = 0L

    // ⭐ Salvar o tipo para usar em updateGif()
    private var tipoCardio: String = ""

    data class Phase(
        val name: String,
        val durationMillis: Long
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_start)

        val btnBack = findViewById<ImageView>(R.id.btnVoltar)
        btnBack.setOnClickListener { finish() }

        tipoCardio = intent.getStringExtra("tipoCardio") ?: "Cardio"
        val intensity = intent.getStringExtra("intensity") ?: "Moderado"
        val durationStr = intent.getStringExtra("duration") ?: "30 minutos"

        findViewById<TextView>(R.id.txtCardioTitle).text = "$tipoCardio — $intensity"
        findViewById<TextView>(R.id.txtCardioInfo).text = "Duração: $durationStr"

        tvTimer = findViewById(R.id.tvTimer)
        btnStartPause = findViewById(R.id.btnStartPause)
        btnReset = findViewById(R.id.btnReset)
        gifImage = findViewById(R.id.gifExercicio)

        tvCurrentPhase = TextView(this).apply {
            textSize = 20f
            setTextColor(resources.getColor(R.color.fire_text, null))
            text = "Aquecimento"
        }

        findViewById<LinearLayout>(R.id.exerciseList).addView(tvCurrentPhase, 0)

        pulseAnimator = ObjectAnimator.ofFloat(tvTimer, View.SCALE_X, 1f, 1.08f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        pulseAnimator?.addUpdateListener { tvTimer.scaleY = tvTimer.scaleX }

        setupPhases(tipoCardio)

        currentPhaseIndex = 0
        timeLeftMillis = phases[0].durationMillis
        updatePhaseText()
        updateTimerText()
        updateGif()

        btnStartPause.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        btnReset.setOnClickListener {
            resetWorkout()
        }
    }

    // ⭐ GIF só aparece em corrida e caminhada
    private fun updateGif() {
        val tipo = tipoCardio.lowercase()

        // Caminhada / Corrida / Bike (já existia)
        if (tipo == "corrida" || tipo == "caminhada" || tipo == "bicicleta") {
            gifImage.visibility = View.VISIBLE

            if (tipo == "bicicleta") {
                Glide.with(this).asGif().load(R.drawable.bike).into(gifImage)
                return
            }

            val fase = phases[currentPhaseIndex].name.lowercase()
            val gifRes = when {
                fase.contains("aquecimento") -> R.drawable.running
                fase.contains("moderada") -> R.drawable.running
                fase.contains("corrida") -> R.drawable.running
                fase.contains("sprint") -> R.drawable.running
                fase.contains("alongamento") -> R.drawable.running
                else -> R.drawable.running
            }

            Glide.with(this).asGif().load(gifRes).into(gifImage)
            return
        }

        // ⭐ NOVOS TIPOS — NÃO REMOVE NADA DO EXISTENTE:
        when (tipo) {

            // SUPERIOR COMPLETO
            "superior total", "superior_total", "superior +" -> {
                gifImage.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.superior_total)
                    .into(gifImage)
            }

            // SUPERIOR
            "superior" -> {
                gifImage.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.superior)
                    .into(gifImage)
            }

            // INFERIOR
            "inferior" -> {
                gifImage.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.inferior)
                    .into(gifImage)
            }

            // QUALQUER OUTRO — ESCONDE (igual já fazia)
            else -> {
                gifImage.visibility = View.GONE
            }
        }
    }


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
                phases += Phase("Aquecimento — Pedalada leve", 5 * 60_000)
                phases += Phase("Pedalada moderada 1/2", 10 * 60_000)
                phases += Phase("Pedalada moderada 2/2", 10 * 60_000)
                phases += Phase("Pedalada intensa", 5 * 60_000)
                phases += Phase("Alongamento final", 5 * 60_000)
            }

            "caminhada" -> {
                phases += Phase("Aquecimento", 5 * 60_000)
                phases += Phase("Caminhada moderada", 10 * 60_000)
                phases += Phase("Caminhada rápida", 10 * 60_000)
                phases += Phase("Alongamento final", 5 * 60_000)
            }

            else -> {
                phases += Phase("Aquecimento", 5 * 60_000)
                phases += Phase("Treino principal", 20 * 60_000)
                phases += Phase("Resfriamento", 5 * 60_000)
            }
        }
    }

    private fun updatePhaseText() {
        tvCurrentPhase.text = "Fase atual: ${phases[currentPhaseIndex].name}"
    }

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
        updateGif()

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
        updateGif()
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
