package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_FINISH_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo.AOEPartTwoViewModel
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.AddRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AOEPhotoViewModel @Inject constructor(private val addRepo: AddRepository) : ViewModel() {

    private val addEditPhotoChannel = Channel<AOEPhotoViewModel.AddEditPhotoEvent>()
    val addEditTwoEvent = addEditPhotoChannel.receiveAsFlow()

    private var listPhotoLiveData: MutableLiveData<List<Photo>> = MutableLiveData()
    private var listPhoto = ArrayList<Photo>()

    fun addPhoto(name: String, location: Uri) {
        listPhoto.add(Photo(name, location))
        listPhotoLiveData.value = listPhoto
    }

    fun listPhotoLive() = listPhotoLiveData

    fun getAllData() {
        createEstateInDB()

    }

    private fun createEstateInDB() = viewModelScope.launch{
        addRepo.createEstateInDatabase()
        addEditPhotoChannel.send(AddEditPhotoEvent.NavigateResult(ADD_EDIT_FINISH_RESULT))
    }

    sealed class AddEditPhotoEvent{
        data class NavigateResult(val result: Int): AddEditPhotoEvent()
    }


//    fun addName(name: String) {
//        mName = name
//    }
}