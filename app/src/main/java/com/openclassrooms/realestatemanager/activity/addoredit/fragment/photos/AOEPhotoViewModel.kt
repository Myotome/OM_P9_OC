package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_FINISH_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AOEPhotoViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditPhotoChannel = Channel<AddEditPhotoEvent>()
    val addEditTwoEvent = addEditPhotoChannel.receiveAsFlow()

    private var listPhotoLiveData = MutableLiveData<MutableList<Photo>>()

    init {
        listPhotoLiveData.value = ArrayList()
    }

    @FlowPreview
    private var currentEstate: LiveData<Estate> =
        roomRepo.getEstateById()!!.asLiveData(coroutineDispatchers.ioDispatchers)

    @FlowPreview
    private var mediator = MediatorLiveData<AOEPhotoViewState>().apply {
        addSource(listPhotoLiveData) { photo -> mediatorCombine(photo, currentEstate.value) }
        addSource(currentEstate) { estate -> mediatorCombine(listPhotoLiveData.value, estate) }
    }

    @FlowPreview
    private fun mediatorCombine(listPhoto: MutableList<Photo>?, value: Estate?) {
        val localList = ArrayList<Photo>()
        var id: Int? = null
        if (value?.listPhoto != null) {
            id = value.id
            for (estatePhoto in value.listPhoto.listIterator()) {
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

    fun addPhoto(name: String, path: String) {
        listPhotoLiveData.value?.add(Photo(name, path))
        listPhotoLiveData.value = listPhotoLiveData.value
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
    private fun createEstateInDB() = viewModelScope.launch {
        mediator.value?.listPhoto?.let { addRepo.createEstateInDatabase(it) }
        addEditPhotoChannel.send(AddEditPhotoEvent.NavigateResult(ADD_EDIT_FINISH_RESULT))
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addEditPhotoChannel.send(AddEditPhotoEvent.ShowInvalidInputMessage("Minimum 1 photo is required"))
    }

    sealed class AddEditPhotoEvent {
        data class NavigateResult(val result: Int) : AddEditPhotoEvent()
        data class ShowInvalidInputMessage(val msg: String) : AddEditPhotoEvent()
    }
}
