package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Interest
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRepository @Inject constructor(
    private val estateDAO: EstateDAO
) {

    private var onSale = true
    private var estateId: Long? = null
    private var type: String? = null
    private var price: Int? = null
    private var surface: Double? = null
    private var rooms: Int? = null
    private var landSize: Double? = null
    private var soldDate: Long? = null
    private var bedrooms: Int? = null
    private var bathrooms: Int? = null
    private var description: String? = null
    private var realtor: String? = null
    private var dateEntry: Long? = null
    private var modifyDate: Long? = null
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var address: Address
    private lateinit var interest: Interest

    fun setPartOne(
        onSale: Boolean,
        type: String,
        price: Int,
        surface: Double,
        rooms: Int?,
        landSize: Double?,
        soldDate: Long?
    ) {
        this.onSale = onSale
        this.type = type
        this.price = price
        this.surface = surface
        this.rooms = rooms
        this.landSize = landSize
        this.soldDate = soldDate
    }

    fun setPartTwo(
        id: Long?,
        bedroom: Int?,
        bathroom: Int?,
        description: String?,
        realtor: String?,
        entryDate: Long?,
        modificationDate: Long?
    ) {
        estateId = id
        bedrooms = bedroom
        bathrooms = bathroom
        this.description = description
        this.realtor = realtor
        dateEntry = entryDate
        modifyDate = modificationDate
    }

    fun setAddress(address: Address, lat: Double, lng: Double) {
        this.address = address
        this.lat = lat
        this.lng = lng
    }

    fun setInterest(interest: Interest) {
        this.interest = interest
    }

//    suspend fun createEstateInDatabase(listPhoto: List<Photo>) {
//        val estate = Estate(
//            estateType = type!!,
//            price = price!!,
//            surface = surface!!,
//            room = rooms,
//            landSize = landSize,
//            bedrooms = bedrooms,
//            bathrooms = bathrooms,
//            description = description,
//            realtor = realtor,
//            address = address,
//            interest = interest,
//            listPhoto = listPhoto,
//            entryDate = dateEntry!!,
//            soldDate = soldDate,
//            modificationDate = modifyDate,
//            onSale = onSale,
//            lat = lat!!,
//            lng = lng!!
//
//        )
//        if (estateId != null) {
//            val updateEstate = estate.copy(id = estateId!!)
//            estateDAO.updateEstate(updateEstate)
////            Log.d("DEBUGKEY", "UpdateEstateInDatabase: estate is update")
//        } else {
//            estateId = Utils.getLongFormatDate()*12
//            estateDAO.insertEstate(estate)
////            Log.d("DEBUGKEY", "createEstateInDatabase: estate created")
//        }
//    }
}