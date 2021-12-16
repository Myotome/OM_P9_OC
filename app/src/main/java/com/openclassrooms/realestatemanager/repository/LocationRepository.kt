package com.openclassrooms.realestatemanager.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Get device's location when permission are okay
 * Position are update every 2 sec and
 * for displacement of 100 m
 */

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
        return currentPosition
    }

    private fun getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(120)
            fastestInterval = TimeUnit.SECONDS.toMillis(60)
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
            return
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
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

