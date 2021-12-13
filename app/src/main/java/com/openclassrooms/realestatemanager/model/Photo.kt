package com.openclassrooms.realestatemanager.model

import com.google.firebase.firestore.Exclude

data class Photo(
    val name: String,
    private var _image: String? = null,
    val storageId: String,
    val storageUriString: String?
) {

    var image: String?
        @Exclude get() {return _image}
        set(value) {_image = value}

    constructor() : this("", null, "", null)
}