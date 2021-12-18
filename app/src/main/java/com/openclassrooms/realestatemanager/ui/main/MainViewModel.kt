package com.openclassrooms.realestatemanager.ui.main

import android.net.Uri
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A very important view model.
 * In fact this view model is use for synchronise local DB and firestore
 * Algorithm check which of the two db must be update
 * Also chek if image was stored online or not, and update if it's need
 */

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
) : ViewModel() {

    fun clearCurrentEstate() = dataSourceRepository.setCurrentEstateById(null)

    fun clearSearch() = dataSourceRepository.setSearchQuery(null)

    private val estateFromRoomDatabase =
        dataSourceRepository.querySearchFlow.asLiveData(Dispatchers.IO)
    private val estateFromFirebase =
        dataSourceRepository.getAllEstateFromFirestore.asLiveData(Dispatchers.IO)

    private var mediator = MediatorLiveData<List<Estate>?>().apply {
        addSource(estateFromFirebase) { firebaseEstate ->
            mediatorCombine(
                firebaseEstate,
                estateFromRoomDatabase.value
            )
        }
        addSource(estateFromRoomDatabase) { roomEstate ->
            mediatorCombine(
                estateFromFirebase.value,
                roomEstate
            )
        }
    }

    fun synchroniseAllDatabase(): LiveData<List<Estate>?> = mediator

    private fun mediatorCombine(firebaseEstate: List<Estate>?, roomEstate: List<Estate>?) =
        viewModelScope.launch(Dispatchers.IO) {

            when {
                firebaseEstate != null && roomEstate == null -> firebaseEstate.forEach { estate ->
                    dataSourceRepository.createInRoomFromFirebase(estate)
                }
                else -> syncAlgo(firebaseEstate, roomEstate)
            }
        }

    private suspend fun syncAlgo(
        firestoreListEstate: List<Estate>?,
        roomListEstate: List<Estate>?
    ) {
        if (firestoreListEstate != null && roomListEstate != null) {
            for (roomEstate in roomListEstate) {
                val roomFId = roomEstate.secondaryEstateData.firebaseId
                val roomModDate = roomEstate.secondaryEstateData.modificationDate
                val firestoreId = firestoreListEstate.map { it.secondaryEstateData.firebaseId }

                if (!firestoreId.contains(roomFId)) {
                    dataSourceRepository.createEstateInFirestore(checkStorageUri(roomEstate))

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
                                    dataSourceRepository.createEstateInFirestore(
                                        checkStorageUri(
                                            roomEstate
                                        )
                                    )

                                }
                            }
                        } else if (roomModDate != null && firestorModDate == null) {
                            dataSourceRepository.createEstateInFirestore(checkStorageUri(roomEstate))


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


    private suspend fun checkStorageUri(estate: Estate): Estate {

        val newListPhoto = mutableSetOf<Photo>()

        estate.listPhoto.forEach { photo ->
            if (photo.storageUriString == null && photo.image != null) {
                var storageUri: String? = null
                dataSourceRepository.setImageToStorage(
                    storageId = photo.storageId,
                    uri = Uri.parse(photo.image)
                ).collect { storageUri = it }
                val newPhoto = storageUri.toString().let { photo.copy(storageUriString = it) }
                newListPhoto.add(newPhoto)


            } else {
                newListPhoto.add(photo)
            }
        }
        return estate.copy(
            listPhoto = newListPhoto.toList(),
            secondaryEstateData = estate.secondaryEstateData.copy(modificationDate = Utils.getLongFormatDate())
        )
    }


}