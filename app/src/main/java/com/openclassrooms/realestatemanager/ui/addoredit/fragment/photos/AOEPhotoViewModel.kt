package com.openclassrooms.realestatemanager.ui.addoredit.fragment.photos

import android.net.Uri
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_FINISH_RESULT
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Main logic for fragment
 * When edition, get data from repository and create view state to show correct data on view
 * Assert all obligatory fields are complete before save part
 * Flow channel to communicate with the view
 * Combine two different sources : repository and view
 * So main logic is on combine method
 */

@DelicateCoroutinesApi
@FlowPreview
@HiltViewModel
class AOEPhotoViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
) : ViewModel() {

    private val addEditPhotoChannel = Channel<AddEditPhotoEvent>()
    val addEditTwoEvent = addEditPhotoChannel.receiveAsFlow()

    private var listPhotoLiveData = MutableLiveData<MutableList<Photo>>()
    private var photoListToDelete = ArrayList<Photo>()

    init {
        listPhotoLiveData.value = ArrayList()
    }

    var currentEstate: LiveData<Estate>? =
        dataSourceRepository.getEstateById()?.asLiveData(Dispatchers.IO)

    private var mediator = MediatorLiveData<AOEPhotoViewState>().apply {
        addSource(listPhotoLiveData) { photo -> mediatorCombine(photo, currentEstate?.value) }
        if (currentEstate != null) addSource(currentEstate!!) { estate ->
            mediatorCombine(listPhotoLiveData.value, estate)
        }
    }

    private fun mediatorCombine(listPhoto: MutableList<Photo>?, value: Estate?, photoToDelete: Photo? = null) {
        val localList = ArrayList<Photo>()
        photoToDelete?.let { photoListToDelete.add(it)}
        var id: Long? = null
        if (value?.listPhoto != null) {
            id = value.id
            for (estatePhoto in value.listPhoto) {
                if(!photoListToDelete.contains(estatePhoto)){
                    localList.add(estatePhoto)
                }
            }
        }
        if (listPhoto != null) {
            for (photo in listPhoto)
                if (!photoListToDelete.contains(photo))localList.add(photo)
        }

       mediator.value = AOEPhotoViewState(id, localList)
    }

    fun listPhotoLive(): LiveData<AOEPhotoViewState> {
        return mediator
    }


    /**
     * When internet is available, get directly storage uri
     * When internet isn't available, save local uri on local database
     * This way permit to not block threat, and get storage uri later
     * with unique ID
     */
    fun addPhoto(internet: Boolean, name: String, path: String, storagePhotoId: String?) =
        viewModelScope.launch {
            val storageId = storagePhotoId ?: UUID.randomUUID().toString()
            if (internet) {
                waitForUpload(true)
                saveOnStorage(name, path, storageId)


            } else {
                listPhotoLiveData.value?.add(Photo(name, path, storageId, null))
                listPhotoLiveData.value = listPhotoLiveData.value
            }
        }

    fun removePhoto(photo: Photo) {
        mediatorCombine(listPhotoLiveData.value, currentEstate?.value, photo)
        dataSourceRepository.deleteInStorage(photo.storageId)
    }

    fun getAllData() {
        if (mediator.value == null || mediator.value?.listPhoto.isNullOrEmpty()) {
            showInvalidInputMessage()
            return
        } else {
            createEstateInDB()
        }
    }


    private fun createEstateInDB() = GlobalScope.launch {
        mediator.value?.listPhoto?.let { dataSourceRepository.createEstateInDatabase(it) }
        addEditPhotoChannel.send(AddEditPhotoEvent.NavigateResult(ADD_EDIT_FINISH_RESULT))
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addEditPhotoChannel.send(AddEditPhotoEvent.ShowInvalidInputMessage("Minimum 1 photo is required"))
    }

    private fun waitForUpload(status : Boolean) = viewModelScope.launch {
        addEditPhotoChannel.send(AddEditPhotoEvent.WaitingForUpload(status))
    }

    private suspend fun saveOnStorage(name: String, path: String, storageId: String) {
        val uri = Uri.parse(path)
        var storageUri: String? = null
        dataSourceRepository.setImageToStorage(storageId, uri).collect {
            storageUri = it
            waitForUpload(false)
        }

        listPhotoLiveData.value?.add(Photo(name, path, storageId, storageUri))
        listPhotoLiveData.value = listPhotoLiveData.value
    }

    sealed class AddEditPhotoEvent {
        data class NavigateResult(val result: Int) : AddEditPhotoEvent()
        data class ShowInvalidInputMessage(val msg: String) : AddEditPhotoEvent()
        data class WaitingForUpload(val upload: Boolean) : AddEditPhotoEvent()
    }
}
