package com.example.gigshield

import android.app.Application
import com.example.gigshield.di.AppContainer

class GigShieldApplication : Application() {
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
