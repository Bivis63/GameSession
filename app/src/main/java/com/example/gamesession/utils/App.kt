package com.example.gamesession.utils

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        AppDependencies.initialize(this)
    }
}