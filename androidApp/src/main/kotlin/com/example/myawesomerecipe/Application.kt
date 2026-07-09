package com.example.myawesomerecipe

import android.app.Application
import com.example.myawesomerecipe.cache.AndroidContextHolder

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContextHolder.applicationContext = this
    }
}