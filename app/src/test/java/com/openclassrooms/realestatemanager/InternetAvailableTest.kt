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
//    private lateinit var typeMobile : ConnectivityManager
//    private lateinit var shadowTypeMobile : ShadowConnectivityManager
//    private lateinit var typeWifi : ShadowNetworkInfo

    @Before
    fun setUp(){
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.activeNetworkInfo)
//        typeMobile =  context.getSystemService(Context.WIFI_SERVICE) as ConnectivityManager
//        shadowTypeMobile = shadowOf(typeMobile)
//        typeWifi = shadowOf(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))
    }

    @Test
    fun getActiveNetworkInfo_shouldInitializeItself(){
        Assert.assertNotNull(connectivityManager.activeNetworkInfo)
    }

    @Test
    fun getInternetIsAvailable(){

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED)
        connectivityManager.activeNetworkInfo
            ?.let {
                Assert.assertTrue(it.isConnectedOrConnecting)
                Assert.assertTrue(it.isConnected)
            }
        Assert.assertTrue(Utils.isInternetAvailable(context))
    }


    @Test
    fun getInternetIsNotAvailable(){

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        connectivityManager.activeNetworkInfo
            ?.let {
                Assert.assertFalse(it.isConnectedOrConnecting)
                Assert.assertFalse(it.isConnected)
            }
        Assert.assertFalse(Utils.isInternetAvailable(context))

    }

    @Test
    fun getNetworkInfo_shouldReturnDefaultNetworks() {
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        Assert.assertTrue(wifi!!.detailedState == NetworkInfo.DetailedState.DISCONNECTED)
        val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        Assert.assertTrue(mobile!!.detailedState == NetworkInfo.DetailedState.CONNECTED)

        Assert.assertTrue(Utils.isInternetAvailable(context))
    }

//    @Test
//    fun mobileOff_WIFIOn(){
//        shadowTypeMobile.
//
//    }

}