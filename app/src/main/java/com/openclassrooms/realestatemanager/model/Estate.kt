package com.openclassrooms.realestatemanager.model

import androidx.room.*

@Entity(tableName = "estate_table")
data class Estate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "estate_id", index = true) val id: Long = 0L,
    @Embedded val primaryEstateData: PrimaryEstateData,
    @Embedded val secondaryEstateData: SecondaryEstateData,
    @Embedded val address: Address,
    val lat: Double,
    val lng: Double,
    val listPhoto: List<Photo>,
    @Embedded val interest: Interest

){}