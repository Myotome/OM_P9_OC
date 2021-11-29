package com.openclassrooms.realestatemanager.model

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