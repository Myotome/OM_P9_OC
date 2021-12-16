package com.openclassrooms.realestatemanager.model

/**
 * Data class for point of interest only
 *
 * No need empty constructor for firebase
 * because all fields are filled by default
 */

data class Interest(
    val school: Boolean = false,
    val store: Boolean = false,
    val park: Boolean = false,
    val restaurant: Boolean = false,
    val movie: Boolean = false,
    val theatre: Boolean = false,
    val subway: Boolean = false,
    val nightlife: Boolean = false
)