package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityEventDetailBinding
import com.example.myapplication.model.Event

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("event_id", -1)
        val event = findEventById(eventId) ?: return

        binding.txtTitle.text = event.title
        binding.txtDate.text = event.date
        binding.txtTime.text = event.time
        binding.imgEvent.setImageResource(event.image)
    }

    private fun findEventById(id: Int): Event? {
        // Aqui você pode usar FakeDatabase ou sua lista de eventos
        val events = listOf(
            Event(1, "Mark Queen", "December 11", "22:15", R.drawable.ic_user_placeholder, "in progress", false),
            Event(2, "O Roki", "December 15", "21:45", R.drawable.ic_user_placeholder, "completed", true)
        )
        return events.find { it.id == id }
    }
}
