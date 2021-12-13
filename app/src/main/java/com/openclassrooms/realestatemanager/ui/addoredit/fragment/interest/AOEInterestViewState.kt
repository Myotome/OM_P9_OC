package com.openclassrooms.realestatemanager.ui.addoredit.fragment.interest

data class AOEInterestViewState(
    val school: Boolean = false,
    val store: Boolean = false,
    val park: Boolean = false,
    val restaurant: Boolean = false,
    val movie: Boolean = false,
    val theatre: Boolean = false,
    val subway: Boolean = false,
    val nightlife: Boolean = false
    )