package com.openclassrooms.realestatemanager.model

data class Address(
    val district: String,
    val number: Int,
    val complement: String? = "",
    val street: String,
    val city: String
)