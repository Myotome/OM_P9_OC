package com.openclassrooms.realestatemanager.model

import androidx.room.*

@Entity(tableName = "estate_table")
data class Estate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "estate_id", index = true) var id: Long = 0L,
    @Embedded val primaryEstateData: PrimaryEstateData,
    @Embedded val secondaryEstateData: SecondaryEstateData,
    @Embedded val address: Address,
    val lat: Double,
    val lng: Double,
    val listPhoto: List<Photo>,
    @Embedded val interest: Interest

){constructor() : this(0L,
    PrimaryEstateData(true,"",0,0.0,null, null, null),
    SecondaryEstateData(0L,null,null,null,null,null,null),
    Address("",0,"", "",""),
    0.0,
    0.0,
    emptyList(),
    Interest()) }
