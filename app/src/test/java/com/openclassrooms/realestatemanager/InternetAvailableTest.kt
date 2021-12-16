package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkInfo

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class InternetAvailableTest {

    private lateinit var connectivityManager : ConnectivityManager
    private lateinit var shadowOfActiveNetworkInfo : ShadowNetworkInfo
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp(){
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.activeNetworkInfo)
    }

    @Test
    fun getActiveNetworkInfo_shouldInitializeItself(){
        Assert.assertNotNull(connectivityManager.activeNetworkInfo)
    }

    @Test
    fun getInternetIsAvailableTest(){

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED)
        connectivityManager.activeNetworkInfo
            ?.let {
                Assert.assertTrue(it.isConnectedOrConnecting)
                Assert.assertTrue(it.isConnected)
            }
        Assert.assertTrue(Utils.isInternetAvailable(context))
    }


    @Test
    fun getInternetIsNotAvailableTest(){

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        connectivityManager.activeNetworkInfo
            ?.let {
                Assert.assertFalse(it.isConnectedOrConnecting)
                Assert.assertFalse(it.isConnected)
            }
        Assert.assertFalse(Utils.isInternetAvailable(context))

    }

    @Test
    fun getInternetWhenIsConnectingTest(){
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTING)
        connectivityManager.activeNetworkInfo?.let {
            Assert.assertTrue(it.isConnectedOrConnecting)
            Assert.assertFalse(it.isConnected)
        }
        Assert.assertFalse(Utils.isInternetAvailable(context))
    }

}