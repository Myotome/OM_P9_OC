package com.openclassrooms.realestatemanager.activity.addoredit.fragment.address

data class AOEAddressViewState(
    val number: Int?,
    val complement: String?,
    val street: String?,
    val district: String,
    val postcode: Int?,
    val city: String
    )