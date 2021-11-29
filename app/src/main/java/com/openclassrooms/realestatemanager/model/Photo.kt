package com.openclassrooms.realestatemanager.model

import android.net.Uri

data class Photo(val name: String, val image: String, val storageId: String, val storageUriString: String?) {
    constructor() : this("", "", "", null)
}