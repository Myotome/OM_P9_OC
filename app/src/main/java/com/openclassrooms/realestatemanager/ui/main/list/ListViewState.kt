package com.openclassrooms.realestatemanager.ui.main.list

import com.openclassrooms.realestatemanager.model.Photo

data class ListViewState(
    val id: Long,
    val type: String,
    val district: String,
    val price: Int,
    val onSale: Boolean,
    val photo: Photo
)
