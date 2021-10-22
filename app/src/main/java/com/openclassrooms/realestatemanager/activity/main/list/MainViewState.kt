package com.openclassrooms.realestatemanager.activity.main.list

import android.net.Uri
import com.openclassrooms.realestatemanager.model.Photo

data class MainViewState(
    val id: Int,
    val type: String,
    val district: String,
    val price: Int,
    val onSale: Boolean,
    val photo: Photo?
)
