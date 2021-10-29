package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Interest
import com.openclassrooms.realestatemanager.model.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRepository @Inject constructor(
    private val estateDAO: EstateDAO
) {

    private var onSale = true
    private var estateId: Int? = null
    private val type = MutableLiveData<String?>()
    private val price = MutableLiveData<Int?>()
    private val surface = MutableLiveData<Double?>()
    private val rooms = MutableLiveData<Int?>()
    private val landSize = MutableLiveData<Double?>()
    private var soldDate: Long? = null
    private val bedrooms = MutableLiveData<Int?>()
    private val bathrooms = MutableLiveData<Int?>()
    private val description = MutableLiveData<String?>()
    private val realtor = MutableLiveData<String?>()
    private var dateEntry: Long? = null
    private var modifyDate: Long? = null

    //    private val addressLiveData = MutableLiveData<Address>()
    private lateinit var address: Address
    private val interestLiveData = MutableLiveData<Interest>()
//    private var listPhoto = ArrayList<Photo>()

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
        this.type.value = type
        this.price.value = price
        this.surface.value = surface
        this.rooms.value = rooms
        this.landSize.value = landSize
        this.soldDate = soldDate
    }

    fun setPartTwo(
        id: Int?,
        bedroom: Int?,
        bathroom: Int?,
        description: String?,
        realtor: String?,
        entryDate: Long?,
        modificationDate: Long?
    ) {
        estateId = id
        bedrooms.value = bedroom
        bathrooms.value = bathroom
        this.description.value = description
        this.realtor.value = realtor
        dateEntry = entryDate
        modifyDate = modificationDate
    }

    fun setAddress(address: Address) {
        this.address = address
    }

    fun setInterest(interest: Interest) {
        interestLiveData.value = interest
    }

    suspend fun createEstateInDatabase(listPhoto: List<Photo>) {
        val estate = Estate(
            estateType = type.value!!,
            price = price.value!!,
            surface = surface.value!!,
            room = rooms.value,
            landSize = landSize.value,
            bedrooms = bedrooms.value,
            bathrooms = bathrooms.value,
            description = description.value,
            realtor = realtor.value,
            address = address,
            interest = interestLiveData.value!!,
            listPhoto = listPhoto,
            entryDate = dateEntry!!,
            soldDate = soldDate,
            modificationDate = modifyDate,
            onSale = onSale
        )
        if (estateId != null) {
            val updateEstate = estate.copy(id = estateId!!)
            estateDAO.updateEstate(updateEstate)
            Log.d("DEBUGKEY", "UpdateEstateInDatabase: estate is update")
        } else {
            estateDAO.insertEstate(estate)
            Log.d("DEBUGKEY", "createEstateInDatabase: estate created")
        }
    }
}