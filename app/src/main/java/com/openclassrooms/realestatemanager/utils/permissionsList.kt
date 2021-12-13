package com.openclassrooms.realestatemanager.utils

import android.Manifest

val appPerms = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

fun permissionNameForUser (permission : String): String =
    when(permission){
        Manifest.permission.ACCESS_COARSE_LOCATION -> { "Coarse Location"}
        Manifest.permission.ACCESS_FINE_LOCATION -> "Fine Location"
        Manifest.permission.CAMERA -> "Camera"
        Manifest.permission.READ_EXTERNAL_STORAGE -> "Read external storage"
        else -> ""
    }