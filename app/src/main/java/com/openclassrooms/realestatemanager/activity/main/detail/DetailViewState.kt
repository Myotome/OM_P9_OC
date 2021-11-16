package com.openclassrooms.realestatemanager.activity.main.detail


import com.openclassrooms.realestatemanager.model.Photo

data class DetailViewState (
    val id: Int,
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
    val nightlife: Boolean,
    val photoList: List<Photo>?,
    val address: String,
    val realtor: String,
    val entryDate: String,
    val modificationDate: String,
    val soldDate: String,
    val onSale: Boolean,
    val formattedAddress: String
)