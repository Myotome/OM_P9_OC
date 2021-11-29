package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_FINISH_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class AOEPhotoViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditPhotoChannel = Channel<AddEditPhotoEvent>()
    val addEditTwoEvent = addEditPhotoChannel.receiveAsFlow()

    private var listPhotoLiveData = MutableLiveData<MutableList<Photo>>()

    init {
        listPhotoLiveData.value = ArrayList()
    }

    @FlowPreview
    private var currentEstate: LiveData<Estate>? =
        dataSourceRepository.getEstateById()?.asLiveData(coroutineDispatchers.ioDispatchers)

    @FlowPreview
    private var mediator = MediatorLiveData<AOEPhotoViewState>().apply {
        addSource(listPhotoLiveData) { photo -> mediatorCombine(photo, currentEstate?.value) }
        if (currentEstate != null) addSource(currentEstate!!) { estate ->
            mediatorCombine(
                listPhotoLiveData.value,
                estate
            )
        }
    }

    @FlowPreview
    private fun mediatorCombine(listPhoto: MutableList<Photo>?, value: Estate?) {
        val localList = ArrayList<Photo>()
        var id: Long? = null
        if (value?.listPhoto != null) {
            id = value.id
            for (estatePhoto in value.listPhoto) {
                localList.add(estatePhoto)
            }
        }
        if (listPhoto != null) {
            for (photo in listPhoto)
                localList.add(photo)
        }
        mediator.value = AOEPhotoViewState(id, localList)
    }

    @FlowPreview
    fun listPhotoLive(): LiveData<AOEPhotoViewState> {
        return mediator
    }

    fun addPhoto(name: String, path: String, internet: Boolean) = viewModelScope.launch {
        val storageId = UUID.randomUUID().toString()
        if (internet) {
            saveOnStorage(name, path, storageId)
        } else {
            listPhotoLiveData.value?.add(Photo(name, path, storageId, null))
            listPhotoLiveData . value = listPhotoLiveData . value
        }
    }

    @FlowPreview
    fun getAllData() {
        if (mediator.value == null || mediator.value?.listPhoto.isNullOrEmpty()) {
            showInvalidInputMessage()
            return
        } else {
            createEstateInDB()
        }
    }

    @FlowPreview
    private fun createEstateInDB() = GlobalScope.launch {
        mediator.value?.listPhoto?.let { dataSourceRepository.createEstateInDatabase(it) }
        addEditPhotoChannel.send(AddEditPhotoEvent.NavigateResult(ADD_EDIT_FINISH_RESULT))
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addEditPhotoChannel.send(AddEditPhotoEvent.ShowInvalidInputMessage("Minimum 1 photo is required"))
    }

    private suspend fun saveOnStorage(name: String, path: String, storageId: String) {
        val uri = Uri.parse(path)
        var storageUri: String? = null
        dataSourceRepository.uploadImageToStorage(storageId, uri).collect {
            storageUri = it
        }
        Log.d(TAG, "saveOnStorage: storageRui ! $storageUri")
        listPhotoLiveData.value?.add(Photo(name, path, storageId, storageUri))
        listPhotoLiveData.value = listPhotoLiveData.value
    }

    sealed class AddEditPhotoEvent {
        data class NavigateResult(val result: Int) : AddEditPhotoEvent()
        data class ShowInvalidInputMessage(val msg: String) : AddEditPhotoEvent()
    }
}
