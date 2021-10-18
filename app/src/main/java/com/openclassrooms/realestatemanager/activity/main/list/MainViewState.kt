package com.openclassrooms.realestatemanager.activity.main.list

import android.net.Uri

data class MainViewState(
    val id: Int,
    val type: String,
    val district: String,
    val price: Int,
    val photo: Uri?
)
