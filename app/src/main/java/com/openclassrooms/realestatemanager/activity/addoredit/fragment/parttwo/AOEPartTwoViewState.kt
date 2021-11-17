package com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo

data class AOEPartTwoViewState(
    val id: Long,
    val firestoreId: Long?,
    val bedroom: Int?,
    val bathroom: Int?,
    val description: String?,
    val realtor: String?,
    val dateEntry: Long
)