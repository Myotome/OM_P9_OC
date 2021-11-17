package com.openclassrooms.realestatemanager.model

data class PrimaryEstateData(
    val onSale: Boolean = true,
    val estateType: String,
    val price: Int,
    val surface: Double,
    val rooms: Int?,
    val landSize: Double?,
    val soldDate: Long?
)