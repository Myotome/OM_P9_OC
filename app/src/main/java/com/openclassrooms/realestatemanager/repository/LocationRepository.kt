package com.openclassrooms.realestatemanager.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val currentPosition = MutableLiveData<LatLng>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    fun getCurrentPosition(): LiveData<LatLng> {
        if (currentPosition.value == null) getDeviceLocation()
        Log.d("DEBUGKEY", "getCurrentPosition: current Position ${currentPosition.value}")
        return currentPosition
    }

    private fun getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            smallestDisplacement = 100F
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(TAG, "onLocationResult: callback is call")
                super.onLocationResult(locationResult)
                val currentLat = locationResult.lastLocation.latitude
                val currentLng = locationResult.lastLocation.longitude
                currentPosition.value = LatLng(currentLat, currentLng)


            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

