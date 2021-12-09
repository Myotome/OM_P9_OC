package com.openclassrooms.realestatemanager.model

data class Photo(val name: String, val image: String, val storageId: String, val storageUriString: String?) {
    constructor() : this("", "", "", null)
}