package com.openclassrooms.realestatemanager.activity.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsforinstrutest.CoroutineDispatchers
import com.openclassrooms.realestatemanager.utilsforinstrutest.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    fun clearCurrentEstate() = dataSourceRepository.setCurrentEstateById(null)

    fun clearSearch() = dataSourceRepository.setSearchQuery(null)


    private val estateFromRoomDatabase =
        dataSourceRepository.querySearchFlow.asLiveData(coroutineDispatchers.ioDispatchers)
    private val estateFromFirebase =
        dataSourceRepository.getAllEstateFromFirestore.asLiveData(coroutineDispatchers.ioDispatchers)

    private var mediator = MediatorLiveData<List<Estate>?>().apply {
        Log.d(TAG, "mediator add source begin")
        addSource(estateFromFirebase) { firebaseEstate ->
            Log.d(TAG, "firebase source : ${firebaseEstate?.size}")
            mediatorCombine(
                firebaseEstate,
                estateFromRoomDatabase.value
            )
        }
        addSource(estateFromRoomDatabase) { roomEstate ->
            Log.d(TAG, "room source : ${roomEstate?.size}: ")
            mediatorCombine(
                estateFromFirebase.value,
                roomEstate
            )
        }
    }

    fun synchroniseAllDatabase(): LiveData<List<Estate>?> = mediator

    private fun mediatorCombine(firebaseEstate: List<Estate>?, roomEstate: List<Estate>?) =
        viewModelScope.launch(Dispatchers.IO) {

            Log.d(
                TAG,
                "mediatorCombine: begin. Firebase size : ${firebaseEstate?.size}, room size ${roomEstate?.size}"
            )
            when {
                firebaseEstate != null && roomEstate == null -> firebaseEstate.forEach { estate ->
                    dataSourceRepository.createInRoomFromFirebase(estate)
                }
//                firebaseEstate == null && roomEstate != null -> roomEstate.forEach { estate ->
//
//                    dataSourceRepository.createEstateInFirestore(
//                        checkStorageUri(estate)
//                    )
//                }
                else -> syncAlgo(firebaseEstate, roomEstate)
            }
        }

    private suspend fun checkStorageUri(estate: Estate): Estate {
        val newListPhoto = mutableSetOf<Photo>()

        estate.listPhoto.forEach { photo ->
            if (photo.storageUriString == null || photo.storageUriString == "raté") {
                var storageUri: String? = null
                dataSourceRepository.setImageToStorage(
                    storageId = photo.storageId,
                    uri = Uri.parse(photo.image)
                ).collect { storageUri = it }
                Log.d(TAG, "checkStorageUri: storageUri : $storageUri")
                val newPhoto = storageUri.toString().let { photo.copy(storageUriString = it) }
                newListPhoto.add(newPhoto)
            } else {
                newListPhoto.add(photo)
            }
        }
        Log.d(TAG, "checkStorageUri: final : ${newListPhoto.size}")
        return estate.copy(listPhoto = newListPhoto.toList(), secondaryEstateData = estate.secondaryEstateData.copy(modificationDate = Utils.getLongFormatDate()))
    }

    private suspend fun syncAlgo(
        firestoreListEstate: List<Estate>?,
        roomListEstate: List<Estate>?
    ) {
        Log.d(TAG, "syncAlgo: begin")
        if (firestoreListEstate != null && roomListEstate != null) {
            for (roomEstate in roomListEstate) {
                val roomFId = roomEstate.secondaryEstateData.firebaseId
                val roomModDate = roomEstate.secondaryEstateData.modificationDate
                val firestoreId = firestoreListEstate.map { it.secondaryEstateData.firebaseId }

                if (!firestoreId.contains(roomFId)) {
                    dataSourceRepository.createEstateInFirestore(roomEstate)
                    checkStorageUri(roomEstate)
                }

                for (firestoreEstate in firestoreListEstate) {
                    val firestoredId = firestoreEstate.secondaryEstateData.firebaseId
                    val firestorModDate = firestoreEstate.secondaryEstateData.modificationDate
                    if (roomFId == firestoredId) {
                        if (roomModDate != null && firestorModDate != null) {
                            when {

                                roomModDate < firestorModDate -> {
                                    firestoreEstate.id = roomEstate.id
                                    dataSourceRepository.updateInRoomFromFirebase(firestoreEstate)
                                }

                                roomModDate > firestorModDate -> {
                                    dataSourceRepository.createEstateInFirestore(roomEstate)
                                    checkStorageUri(roomEstate)
                                }
                            }
                        } else if (roomModDate != null && firestorModDate == null) {
                            dataSourceRepository.createEstateInFirestore(roomEstate)
                            checkStorageUri(roomEstate)

                        } else if (roomModDate == null && firestorModDate != null) {
                            firestoreEstate.id = roomEstate.id
                            dataSourceRepository.updateInRoomFromFirebase(firestoreEstate)
                        }
                    }
                }
            }

            for (firebaseEstate in firestoreListEstate) {
                val roomFireId = roomListEstate.map { it.secondaryEstateData.firebaseId }
                if (!roomFireId.contains(firebaseEstate.secondaryEstateData.firebaseId)) {
                    dataSourceRepository.createInRoomFromFirebase(firebaseEstate)
                }
            }
        }
    }

}