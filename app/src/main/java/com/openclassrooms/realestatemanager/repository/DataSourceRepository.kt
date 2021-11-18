package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceRepository @Inject constructor(private val estateDAO: EstateDAO) {

    /**
     * ------------------------------------------------------------
     * -------------------Room Database Part-----------------------
     * ------------------------------------------------------------
     */

    /**
     * Get all estate or estate by search query
     */
    val isSearching = MutableLiveData(false)

    private val querySearchMutableStateFlow = MutableStateFlow<SimpleSQLiteQuery?>(null)
    val querySearchFlow = querySearchMutableStateFlow.asSharedFlow().flatMapLatest { query ->
        if (query != null) estateDAO.getSearchEstate(query) else estateDAO.getAllEstate()
    }

    fun setSearchQuery(query: SimpleSQLiteQuery?, searchStatus: Boolean = false) {
        querySearchMutableStateFlow.tryEmit(query)
        isSearching.value = searchStatus
    }

    /**
     * Get one estate by id
     */
    private val currentEstateId = MutableLiveData<Long?>(null)

    fun getEstateById(): Flow<Estate>? {
        return if (currentEstateId.value != null) estateDAO.getCurrentEstate(currentEstateId.value!!) else null
    }

    fun setCurrentEstateById(estateId: Long?) {
        currentEstateId.value = estateId
    }

    /**
     * Create or update estate
     */

    private var id: Long? = null
    private lateinit var primaryEstateData: PrimaryEstateData
    private lateinit var secondaryEstateData: SecondaryEstateData
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var address: Address
    private lateinit var interest: Interest

    fun setPartOne(primaryData: PrimaryEstateData) {
        primaryEstateData = primaryData
    }

    fun setPartTwo(secondData: SecondaryEstateData, estateId: Long?) {
        secondaryEstateData = secondData
        id = estateId
    }

    fun setAddress(address: Address, lat: Double, lng: Double) {
        this.address = address
        this.lat = lat
        this.lng = lng
    }

    fun setInterest(interest: Interest) {
        this.interest = interest
    }

    suspend fun createEstateInDatabase(listPhoto: List<Photo>) {
        val estate = Estate(
            primaryEstateData = primaryEstateData,
            secondaryEstateData = secondaryEstateData,
            address = address,
            lat = lat!!,
            lng = lng!!,
            interest = interest,
            listPhoto = listPhoto
        )
        if (id != null) {
            val updateEstate = estate.copy(id = id!!, secondaryEstateData =( secondaryEstateData.copy(firebaseId = secondaryEstateData.firebaseId)))
            estateDAO.updateEstate(updateEstate)
            createEstateInFirestore(updateEstate)
            Log.d("DEBUGKEY", "UpdateEstateInDatabase: estate is update")
        } else {
            estateDAO.insertEstate(estate)
            createEstateInFirestore(estate)
            Log.d("DEBUGKEY", "createEstateInDatabase: estate created")
        }
    }

    /**
     * update latlng if necessary
     */
    suspend fun updateLatLngById(id: Long, lat: Double, lng: Double) {
        estateDAO.updateLatLngById(id, lat, lng)
    }

    /**
     * ------------------------------------------------------------
     * -------------------Firestore Part---------------------------
     * ------------------------------------------------------------
     */

    private val db = Firebase.firestore

    fun createEstateInFirestore(estate: Estate) {
        db.collection("RealEstatesManager").document(estate.secondaryEstateData.firebaseId.toString()).set(estate)
    }

    val getAllEstateFromFirestore: Flow<List<Estate>?> = callbackFlow {
        db.collection("RealEstatesManager")
            .addSnapshotListener { value, error ->
                when {
                    error != null -> Log.d(TAG,"getAllEstateFromFirestore: error : ${error.message}")
                    value != null -> {
                        val estates = ArrayList<Estate>()
                        for (doc in value) {
                            estates.add(doc.toObject(Estate::class.java))
                            trySend(estates)
                        }

                    }
                }
            }
    }
}


