package com.openclassrooms.realestatemanager.ui.addoredit.fragment.address

data class AOEAddressViewState(
    val number: Int?,
    val complement: String?,
    val street: String?,
    val district: String,
    val city: String,
    val lat: Double,
    val lng: Double
    )