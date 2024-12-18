package com.nicholssoftware.jonslearningappandroid

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }
}