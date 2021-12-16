package com.openclassrooms.realestatemanager.model

/**
 * Data class for address only
 *
 * Empty constructor needed for firebase
 * Do not delete
 */

data class Address(
    val district: String,
    val number: Int,
    val complement: String? = "",
    val street: String,
    val city: String
) {
    constructor() : this("", 0, null, "", "")
}