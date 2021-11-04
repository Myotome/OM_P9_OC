package com.openclassrooms.realestatemanager.activity.main.maps

data class MapsPositionViewState(
    val currentLat: Double,
    val currentLng: Double,
    val listEstate: List<MapsEstateViewState>
)