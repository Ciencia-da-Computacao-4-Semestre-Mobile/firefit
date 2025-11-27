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
    private var timeLeftMillis: Long = 0L
    private var isRunning = false

    private lateinit var tvTimer: TextView
    private lateinit var btnStartPause: Button
    private lateinit var btnReset: Button
    private var pulseAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_start)

        val tipo = intent.getStringExtra("tipoCardio") ?: "Cardio"
        val intensity = intent.getStringExtra("intensity") ?: "Moderado"
        var duration = intent.getStringExtra("duration") ?: "30 minutos"

        findViewById<TextView>(R.id.txtCardioTitle).text = "$tipo — $intensity"
        findViewById<TextView>(R.id.txtCardioInfo).text = "Duração: $duration"

        timeLeftMillis = parseDurationToMillis(duration)

        tvTimer = findViewById(R.id.tvTimer)
        btnStartPause = findViewById(R.id.btnStartPause)
        btnReset = findViewById(R.id.btnReset)

        updateTimerText()

        btnStartPause.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        btnReset.setOnClickListener {
            resetTimer()
        }

        // animação pulse
        val pulseAnimator = ObjectAnimator.ofFloat(tvTimer, View.SCALE_X, 1f, 1.08f).apply {
            setDuration(600)
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }

        pulseAnimator?.addUpdateListener { tvTimer.scaleY = tvTimer.scaleX }

        // ==============================
        // 🔥 AQUI — ADICIONAR LISTA DE EXERCÍCIOS
        // ==============================
        val exerciseContainer = findViewById<LinearLayout>(R.id.exerciseList)
        exerciseContainer.removeAllViews()

        when (tipo.lowercase()) {

            "corrida" -> {
                exerciseContainer.addView(makeTextView("Aquecimento — Corrida leve 5 minutos"))
                exerciseContainer.addView(makeTextView("Corrida moderada — 3×5 minutos"))
                exerciseContainer.addView(makeTextView("Sprints — 5×30 segundos"))
                exerciseContainer.addView(makeTextView("Corrida leve — Recuperação 3 minutos"))
                exerciseContainer.addView(makeTextView("Alongamento final — 5 minutos"))
            }

            "bicicleta" -> {
                exerciseContainer.addView(makeTextView("Aquecimento — Pedalada leve 7 minutos"))
                exerciseContainer.addView(makeTextView("Subidas — 4×4 minutos"))
                exerciseContainer.addView(makeTextView("Pedalada forte — 3×3 minutos"))
                exerciseContainer.addView(makeTextView("Ritmo leve — 5 minutos"))
                exerciseContainer.addView(makeTextView("Alongamento final para pernas — 5 minutos"))
            }

            "caminhada" -> {
                exerciseContainer.addView(makeTextView("Caminhada leve — 10 minutos"))
                exerciseContainer.addView(makeTextView("Caminhada inclinada — 5 minutos"))
                exerciseContainer.addView(makeTextView("Caminhada rápida — 3×3 minutos"))
                exerciseContainer.addView(makeTextView("Caminhada leve — Recuperação 5 minutos"))
                exerciseContainer.addView(makeTextView("Alongamento geral — 5 minutos"))
            }

            else -> {
                exerciseContainer.addView(makeTextView("Aquecimento — 5 minutos"))
                exerciseContainer.addView(makeTextView("Treino principal — 20 minutos"))
                exerciseContainer.addView(makeTextView("Resfriamento — 5 minutos"))
            }
        }
    }

    // ==============================
    // 🔥 FUNÇÃO QUE CRIA TEXTVIEW DOS EXERCÍCIOS
    // ==============================
    private fun makeTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(resources.getColor(R.color.fire_text, null))
            setPadding(0, 6, 0, 6)
        }
    }

    // ==============================
    // ⏱ LÓGICA DO TIMER
    // ==============================
    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object: CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isRunning = false
                updateButtons()
                updateTimerText()
                pulseAnimator?.cancel()

                try {
                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val ring = RingtoneManager.getRingtone(applicationContext, notification)
                    ring.play()
                } catch (_: Exception) { }

                findViewById<View>(R.id.containerStart)
                    .setBackgroundResource(android.R.color.holo_green_light)

                findViewById<TextView>(R.id.txtFinished).visibility = View.VISIBLE
            }
        }.start()

        isRunning = true
        updateButtons()
        pulseAnimator?.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        updateButtons()
        pulseAnimator?.cancel()
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        val text = findViewById<TextView>(R.id.txtCardioInfo)
            .text.toString().replace("Duração: ", "")
        timeLeftMillis = parseDurationToMillis(text)
        updateTimerText()
        isRunning = false
        updateButtons()
        pulseAnimator?.cancel()
        findViewById<View>(R.id.containerStart).setBackgroundResource(android.R.color.transparent)
        findViewById<TextView>(R.id.txtFinished).visibility = View.GONE
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

    private fun parseDurationToMillis(duration: String): Long {
        val num = duration.split(" ")[0].toIntOrNull() ?: 30
        return num * 60L * 1000L
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        pulseAnimator?.cancel()
    }
}
