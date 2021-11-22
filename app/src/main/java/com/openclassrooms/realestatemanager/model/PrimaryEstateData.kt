package com.openclassrooms.realestatemanager.model

data class PrimaryEstateData(
    val onSale: Boolean = true,
    val estateType: String,
    val price: Int,
    val surface: Double,
    val rooms: Int?,
    val landSize: Double?,
    val soldDate: Long?
) {
    constructor() : this(true, "", 0, 0.0, null, null, null)
}