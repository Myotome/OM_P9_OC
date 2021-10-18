package com.openclassrooms.realestatemanager.model

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