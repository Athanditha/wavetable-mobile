package com.example.wavetable

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize

class WaveTableApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
    }
} 