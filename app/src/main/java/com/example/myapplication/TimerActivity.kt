package com.example.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var btnPause: Button
    private lateinit var btnFinish: Button

    private var timer: CountDownTimer? = null
    private var timeLeft = 30 * 60 * 1000L // 30 minutos
    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        txtTimer = findViewById(R.id.txtTimer)
        btnPause = findViewById(R.id.btnPause)
        btnFinish = findViewById(R.id.btnFinish)

        startTimer()

        btnPause.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        btnFinish.setOnClickListener {
            finish()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                txtTimer.text = "00:00"
                btnPause.text = "CONCLUÍDO"
                btnPause.isEnabled = false
            }
        }.start()

        btnPause.text = "PAUSAR"
        isRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        btnPause.text = "CONTINUAR"
        isRunning = false
    }

    private fun updateTimerText() {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60
        txtTimer.text = String.format("%02d:%02d", minutes, seconds)
    }
}
