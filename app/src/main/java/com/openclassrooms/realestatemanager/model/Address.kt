package com.openclassrooms.realestatemanager.model

data class Address(
    val district: String,
    val number: Int? = 0,
    val complement: String? = "",
    val street: String? = "",
    val postCode: Int? = 0,
    val city: String
)