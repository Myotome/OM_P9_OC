package com.openclassrooms.realestatemanager.model

/**
 * Data class for first part of estate only
 * This part is useful in create or update estate
 *
 * Empty constructor needed for firebase
 * Do not delete
 */

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