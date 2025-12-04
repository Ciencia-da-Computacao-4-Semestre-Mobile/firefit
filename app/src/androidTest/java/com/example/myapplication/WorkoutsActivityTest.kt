package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ActivityScenario
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutsActivityTest {

    @Test
    fun launchActivity_success() {
        val scenario = ActivityScenario.launch(WorkoutsActivity::class.java)
        assertTrue(true)  // apenas valida que abriu sem crash
        scenario.close()
    }
}
