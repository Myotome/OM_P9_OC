package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternetConnectionTest {

//    private lateinit var connectivityManager : ConnectivityManager
//    private lateinit var shadowOfActiveNetworkInfo : shadowOf

    val connectivityManager = getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    val networkCapabilities = ShadowNetworkCapabilities.newInstance()

    @Test
    fun internetIsAvailable() {
        Assert.assertTrue(Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().context))
    }
}