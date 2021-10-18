package com.openclassrooms.realestatemanager.activity.main.detail

import com.openclassrooms.realestatemanager.model.Interest

data class DetailViewState (
    val type: String,
    val price: Int,
    val surface: Double,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val landSize: Double?,
    val school: Boolean,
    val store: Boolean,
    val park: Boolean,
    val restaurant: Boolean,
    val movie: Boolean,
    val theatre: Boolean,
    val subway: Boolean,
    val nightlife: Boolean
)