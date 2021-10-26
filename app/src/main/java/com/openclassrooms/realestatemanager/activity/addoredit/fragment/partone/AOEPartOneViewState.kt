package com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone

data class AOEPartOneViewState(
    val onSale: Boolean,
    val type: String,
    val price: Int,
    val surface: Double,
    val room: Int?,
    val landSize: Double?
)