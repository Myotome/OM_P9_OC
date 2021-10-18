package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Interest
import com.openclassrooms.realestatemanager.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRepository @Inject constructor(
    private val estateDAO: EstateDAO
) {

    private var estateId: Int? = null
    private val type = MutableLiveData<String?>()
    private val price = MutableLiveData<Int?>()
    private val surface = MutableLiveData<Double?>()
    private val rooms = MutableLiveData<Int?>()
    private val landsize = MutableLiveData<Double?>()
    private val bedrooms = MutableLiveData<Int?>()
    private val bathrooms = MutableLiveData<Int?>()
    private val description = MutableLiveData<String?>()
    private val realtor = MutableLiveData<String?>()
    private val addressLiveData = MutableLiveData<Address>()
    private val interestLiveData = MutableLiveData<Interest>()

    fun setPartOne(type: String, price: Int, surface: Double, rooms: Int?, landsize: Double?) {
        this.type.value = type
        this.price.value = price
        this.surface.value = surface
        this.rooms.value = rooms
        this.landsize.value = landsize
    }

    fun setPartTwo(
        id: Int?,
        bedroom: Int?,
        bathroom: Int?,
        description: String?,
        realtor: String?
    ) {
        estateId = id
        bedrooms.value = bedroom
        bathrooms.value = bathroom
        this.description.value = description
        this.realtor.value = realtor
    }

    fun setAddress(address: Address) {
        addressLiveData.value = address
    }

    fun setInterest(interest: Interest) {
        interestLiveData.value = interest
    }

    suspend fun createEstateInDatabase() {
        val estate = Estate(
            estateType = type.value!!,
            price = price.value!!,
            surface = surface.value!!,
            room = rooms.value,
            landSize = landsize.value,
            bedrooms = bedrooms.value,
            bathrooms = bathrooms.value,
            description = description.value,
            realtor = realtor.value,
            address = addressLiveData.value,
            interest = interestLiveData.value!!,
            listPhoto = null,
            entryDate = Utils.getTodayDate(),
            soldDate = null
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