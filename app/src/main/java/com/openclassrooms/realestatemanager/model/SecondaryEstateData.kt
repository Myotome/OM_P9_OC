package com.openclassrooms.realestatemanager.model

/**
 * Data class for second part of estate only
 * This part is useful in create or update estate
 *
 * Empty constructor needed for firebase
 * Do not delete
 */

@Suppress("unused")
data class SecondaryEstateData(
    val firebaseId: String,
    val bedroom: Int?,
    val bathroom: Int?,
    val description: String?,
    val realtor: String?,
    val entryDate: Long?,
    val modificationDate: Long?
) {
    constructor() : this("", null, null, null, null, null, null)
}