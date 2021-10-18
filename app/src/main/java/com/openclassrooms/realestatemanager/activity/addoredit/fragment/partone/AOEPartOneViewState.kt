package com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone

data class AOEPartOneViewState(
    val type: String,
    val price: Int,
    val surface: Double,
    val room: Int?,
    val landsize: Double?
)