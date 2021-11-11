package com.openclassrooms.realestatemanager.activity.main.list

import com.openclassrooms.realestatemanager.model.Photo

data class ListViewState(
    val id: Int,
    val type: String,
    val district: String,
    val price: Int,
    val onSale: Boolean,
    val photo: Photo
)
