package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.database.EstateRoomDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

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