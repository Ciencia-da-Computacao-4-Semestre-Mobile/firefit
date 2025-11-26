package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.EventAdapter
import com.example.myapplication.databinding.ActivityEventsScheduledBinding
import com.example.myapplication.model.Event
import com.google.android.material.bottomnavigation.BottomNavigationView

class EventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventsScheduledBinding
    private val eventList = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventsScheduledBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadEvents()

        val adapter = EventAdapter(
            events = eventList,
            onSchedule = { event ->
                val intent = Intent(this, EventDetailActivity::class.java)
                intent.putExtra("event_id", event.id)
                startActivity(intent)
            },
            onCancel = { event ->
                eventList.remove(event)
                binding.recyclerEvents.adapter?.notifyDataSetChanged()
            }
        )

        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)
        binding.recyclerEvents.adapter = adapter

        // ------------------------------------------------------------
        // ⭐ BOTTOM NAVIGATION ADICIONADO
        // ------------------------------------------------------------
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_events

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_training -> {
                    if (this !is WorkoutsActivity) {
                        startActivity(Intent(this, WorkoutsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_home -> {
                    if (this !is HomeActivity) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_events -> {
                    if (this !is EventsActivity) {
                        startActivity(Intent(this, EventsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                R.id.nav_user -> {
                    if (this !is UserActivity) {
                        startActivity(Intent(this, UserActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }

                else -> false
            }
        }
        // ------------------------------------------------------------
    }

    private fun loadEvents() {
        eventList.add(
            Event(
                id = 1,
                title = "Mark Queen",
                date = "December 11",
                time = "22:15",
                image = R.drawable.ic_user_placeholder,
                status = "in progress",
                isFinished = false
            )
        )

        eventList.add(
            Event(
                id = 2,
                title = "O Roki",
                date = "December 15",
                time = "21:45",
                image = R.drawable.ic_user_placeholder,
                status = "completed",
                isFinished = true
            )
        )
    }
}
