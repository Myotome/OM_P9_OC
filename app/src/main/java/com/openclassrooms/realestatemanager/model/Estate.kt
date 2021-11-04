package com.openclassrooms.realestatemanager.model

import androidx.room.*

@Entity(tableName = "estate_table")
data class Estate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "estate_id", index = true) val id: Int = 0,
    val estateType: String,
    val price: Int,
    val surface: Double,
    val room: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val landSize: Double?,
    val onSale: Boolean = true,
    val entryDate: Long,
    val modificationDate: Long?,
    val soldDate: Long?,
    val realtor: String?,
    @Embedded val address: Address,
    val lat: Double,
    val lng: Double,
    val listPhoto: List<Photo>,
    @Embedded val interest: Interest

){}