package com.openclassrooms.realestatemanager.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.*
import com.openclassrooms.realestatemanager.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

const val COLLECTION_PATH = "RealEstatesManager"
const val CHANNEL_ID = "channelId"
const val CHANNEL_NAME = "channelName"
const val NOTIF_ID = 976431

@Singleton
class DataSourceRepository @Inject constructor(
    private val estateDAO: EstateDAO,
    @ApplicationContext private val context: Context
) {

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

    @ExperimentalCoroutinesApi
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
            sendNotification(true)

        } else {
            estateDAO.insertEstate(estate)
            createEstateInFirestore(estate)
            sendNotification(false)

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
     * create estate from firebase synchronisation
     */

    suspend fun createInRoomFromFirebase(estate: Estate) {
        estateDAO.insertEstate(estate)
    }

    /**
     * update estate from firebase synchronisation
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
        db.collection(COLLECTION_PATH)
            .document(estate.secondaryEstateData.firebaseId).set(estate)
    }

    private fun updateLatLngInFirestore(firebaseId: String, lat: Double, lng: Double) {
        db.collection(COLLECTION_PATH).document(firebaseId)
            .update("lat", lat, "lng", lng)
    }

    @ExperimentalCoroutinesApi
    val getAllEstateFromFirestore: Flow<List<Estate>?> = callbackFlow {

        db.collection(COLLECTION_PATH)
            .addSnapshotListener { value, error ->
                val estates = ArrayList<Estate>()
                when {
                    error != null -> {
                        close(error)
                        return@addSnapshotListener
                    }
                    value != null -> {
                        for (doc in value) {
                            estates.add(doc.toObject())
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
            .addOnFailureListener { addError -> close(addError) }
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

    fun deleteInStorage(storageId: String) {
        storageRef.child(storageId).delete()
    }

    /**
     * ------------------------------------------------------------
     * -------------------Notification Part------------------------
     * ------------------------------------------------------------
     */

    private fun sendNotification(isUpdate: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

        var text = "New Estate Created"
        if (isUpdate) text = "Estate update"

        val notificationManager = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Real Estate Manager")
            .setContentText(text)
            .setContentIntent(createPendingIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_baseline_holiday_village_24)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_ID, notification)

    }

    private fun createPendingIntent(): PendingIntent?{
        val intent = Intent(context, MainActivity::class.java)
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(753951, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }


}


