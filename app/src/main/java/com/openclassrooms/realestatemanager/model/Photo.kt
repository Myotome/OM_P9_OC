package com.openclassrooms.realestatemanager.model

import com.google.firebase.firestore.Exclude

/**
 * Data class for photo only
 *
 * var image permit to not save local uri on firebase
 * but it's important tu keep this field in case where
 * is no internet available
 *
 * Empty constructor needed for firebase
 * Do not delete
 */

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