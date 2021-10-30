package com.openclassrooms.realestatemanager.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    companion object{
        lateinit var instance: Application
        private set
    }

    init {
        instance = this
    }
}