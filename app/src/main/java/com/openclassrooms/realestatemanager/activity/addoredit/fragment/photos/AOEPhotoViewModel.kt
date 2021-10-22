package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_FINISH_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone.AOEPartOneViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AOEPhotoViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditPhotoChannel = Channel<AOEPhotoViewModel.AddEditPhotoEvent>()
    val addEditTwoEvent = addEditPhotoChannel.receiveAsFlow()

    private var listPhotoLiveData: MutableLiveData<Photo> = MutableLiveData<Photo>()

    @FlowPreview
    private var currentEstate: LiveData<Estate> =
        roomRepo.estateById.asLiveData(coroutineDispatchers.ioDispatchers)
//    private var listPhoto = ArrayList<Photo>()

    @FlowPreview
    private var mediator = MediatorLiveData<AOEPhotoViewState>().apply {
        addSource(listPhotoLiveData) { photo -> mediatorCombine(photo, currentEstate.value) }
        addSource(currentEstate) { estate -> mediatorCombine(listPhotoLiveData.value, estate) }
    }

    @FlowPreview
    private fun mediatorCombine(photo: Photo?, value: Estate?) {
        val localList = ArrayList<Photo>()
        if (value?.listPhoto != null) {
            for (estatePhoto in value.listPhoto.listIterator()) {
                localList.add(estatePhoto)
            }
        }
        if (photo != null) localList.add(photo)
//        listPhoto = localList
        mediator.value = AOEPhotoViewState(localList)
    }

    @FlowPreview
    fun listPhotoLive(): LiveData<AOEPhotoViewState> = mediator

    fun addPhoto(name: String, path: String) {
        listPhotoLiveData.value = Photo(name, path)
    }
//    @FlowPreview
//    private val currentEstate = roomRepo.estateById.mapNotNull { estate -> map(estate) }.asLiveData(coroutineDispatchers.ioDispatchers)
//
//    private fun map(estate: Estate) : List<Photo>? = estate.listPhoto
//
//    private fun map(estate: Estate): AOEPhotoViewState =
//        AOEPhotoViewState(listPhoto = estate.listPhoto)


//    @FlowPreview
//    fun listPhotoLive(): LiveData<AOEPhotoViewState> =
//        mediatorCombine(listPhotoLiveData, currentEstate)


//    private fun mediatorCombine(
//        listPhotoLiveData: MutableLiveData<Photo>,
//        currentEstate: LiveData<List<Photo>>
//    ): LiveData<List<Photo>?> {
//        val result = MediatorLiveData<List<Photo>?>()
//        val currentList = ArrayList<Photo>()
//        val transformPhoto = Observer<Photo>{
//            listPhotoLiveData.value?.let {
//                photo -> currentList.add(photo)}
//            }
//        val getFromEstate = Observer<List<Photo>>{
//            currentList += (currentEstate.value as ArrayList<Photo>)
//        }
//
//        result.addSource(listPhotoLiveData, transformPhoto)
//        result.addSource(currentEstate, getFromEstate)
//
//        listPhoto = currentList
//
//        return result
//    }


//    private fun mediatorCombine(
//        photoLiveData: MutableLiveData<Photo>,
//        currentEstate: LiveData<Estate>
//    ): LiveData<AOEPhotoViewState> {
//        val result = MediatorLiveData<AOEPhotoViewState>()
//        val currentList = ArrayList<Photo>()
//        val transformPhoto = Observer<Photo> {
//            photoLiveData.value?.let { it1 -> currentList.add(it1) }
//            result.value = AOEPhotoViewState(currentList)
//        }
//        val transformEstate = Observer<Estate> {
//            for (photo in currentEstate.value?.listPhoto!!) {
//                currentList.add(photo)
//            }
//            result.value = AOEPhotoViewState(currentList)
//        }
//
//        result.addSource(photoLiveData, transformPhoto)
//        result.addSource(currentEstate, transformEstate)
//
//        listPhoto = currentList
//
//        return result
//    }


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
        data class ShowInvalidInputMessage(val msg: String): AddEditPhotoEvent()
    }
}