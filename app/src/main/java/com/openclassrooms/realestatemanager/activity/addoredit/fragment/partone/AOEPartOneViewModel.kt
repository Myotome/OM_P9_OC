package com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.address.AOEAddressViewState
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AOEPartOneViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
): ViewModel() {

    private val addEditOneChannel = Channel<AddEditOneEvent>()
    val addEditOneEvent = addEditOneChannel.receiveAsFlow()

    var type: String =""
    var price: Int = -1
    var surface: Double = -1.0
    var rooms: Int? = null
    var landsize: Double? = null

    @FlowPreview
    val currentEstate : LiveData<AOEPartOneViewState?> = roomRepo.estateById.mapNotNull { estate -> map(estate) }
        .asLiveData(coroutineDispatchers.ioDispatchers)

    private fun map(estate: Estate?) : AOEPartOneViewState? = if (estate?.address != null){
        AOEPartOneViewState(type = estate.estateType,
            price = estate.price,
            surface = estate.surface,
            room = estate.room,
            landsize = estate.landSize
        )
    }else{
        null
    }

    fun onSaveClick(){
        when{
            type.isBlank()-> {
                showInvalidInputMessage("Property type cannot be empty")
                return
            }
            (price < 0) -> {
                showInvalidInputMessage("Price cannot be empty")
                return
            }
            (surface < 0.0) ->{
                showInvalidInputMessage("Surface cannot be empty")
                return
            }
            else ->{
                createPartOne()
            }
        }
    }

    private fun createPartOne() = viewModelScope.launch {
        addRepo.setPartOne(type, price, surface, rooms, landsize)
        addEditOneChannel.send(AddEditOneEvent.NavigateWithResult(ADD_EDIT_NEXT_RESULT))
    }

    private fun showInvalidInputMessage(text : String) = viewModelScope.launch {
        addEditOneChannel.send(AddEditOneEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditOneEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditOneEvent()
        data class NavigateWithResult(val result: Int): AddEditOneEvent()
    }
}