package com.openclassrooms.realestatemanager.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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
     * Create or update estate from AOEActivity
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
            val updateEstate = estate.copy(
                id = id!!,
                secondaryEstateData = (secondaryEstateData.copy(firebaseId = secondaryEstateData.firebaseId))
            )
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
    suspend fun updateLatLngById(
        id: Long,
        lat: Double,
        lng: Double,
        firestorId: String,
        modDate: Long
    ) {
        estateDAO.updateLatLngById(id, lat, lng, modDate)
        updateLatLngInFirestore(firestorId, lat, lng)
    }

    /**
     * create estate from firebase sync
     */

    suspend fun createInRoomFromFirebase(estate: Estate) {
        estateDAO.insertEstate(estate)
    }

    /**
     * update estate from firebase sync
     */

    suspend fun updateInRoomFromFirebase(estate: Estate) {
        estateDAO.updateEstate(estate)
    }

    /**
     * ------------------------------------------------------------
     * -------------------Firestore Part---------------------------
     * ------------------------------------------------------------
     */

    private val db = Firebase.firestore

    fun createEstateInFirestore(estate: Estate) {
        db.collection("RealEstatesManager")
            .document(estate.secondaryEstateData.firebaseId).set(estate)
            .addOnSuccessListener { Log.d(TAG, "createEstateInFirestore: succes") }
            .addOnFailureListener { Log.d(TAG, "createEstateInFirestore: fail") }
    }

    private fun updateLatLngInFirestore(firebaseId: String, lat: Double, lng: Double) {
        db.collection("RealEstatesManager").document(firebaseId)
            .update("lat", lat, "lng", lng)
            .addOnSuccessListener { Log.d(TAG, "updateLatLngInFirestore: success") }
            .addOnFailureListener { Log.d(TAG, "updateLatLngInFirestore: fail") }
    }

    @ExperimentalCoroutinesApi
    val getAllEstateFromFirestore: Flow<List<Estate>?> = callbackFlow {
        Log.d(TAG, "getEstatefromFirebase beginning: ")
        db.collection("RealEstatesManager")
            .addSnapshotListener { value, error ->
                val estates = ArrayList<Estate>()
                when {
                    error != null -> {
                        Log.d(TAG, "getAllEstateFromFirestore: error : ${error.message}")
                        close(error)
                        return@addSnapshotListener
                    }
                    value != null -> {
                        Log.d(TAG, "value not null begin: ")
                        for (doc in value) {
                            estates.add(doc.toObject())
                            Log.d(TAG, "value not null for loop. Estate size: ${estates.size}")
                        }
                    }
                }
                trySend(estates).isSuccess

            }
        awaitClose()
    }

    /**
     * ------------------------------------------------------------
     * -------------------Storage Part-----------------------------
     * ------------------------------------------------------------
     */

    private val storageRef = Firebase.storage.reference

    fun setImageToStorage(storageId: String, uri: Uri) = callbackFlow {
        storageRef.child(storageId).putFile(uri)
            .addOnFailureListener { addError -> close(addError)}
            .addOnSuccessListener {
                storageRef.child(storageId).downloadUrl
                    .addOnFailureListener { e -> close(e) }
                    .addOnSuccessListener { uri ->
                        trySend(uri.toString()).isSuccess
                        close()
                    }
            }
        awaitClose()
    }

    fun deleteInStorage(storageId: String){
        storageRef.child(storageId).delete()
    }
}


