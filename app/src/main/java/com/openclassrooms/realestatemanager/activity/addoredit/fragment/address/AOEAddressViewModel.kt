package com.openclassrooms.realestatemanager.activity.addoredit.fragment.address

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.openclassrooms.realestatemanager.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AOEAddressViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditAddressChannel  = Channel<AddEditAddressEvent>()
    val addEditAddressEvent = addEditAddressChannel.receiveAsFlow()

    var district: String = ""
    var number: Int? = null
    var complement: String? = null
    var street: String? = null
    var postCode: Int? = null
    var city: String = ""


    @FlowPreview
    val currentEstate : LiveData<AOEAddressViewState?> = roomRepo.estateById.mapNotNull { estate -> map(estate) }
        .asLiveData(coroutineDispatchers.ioDispatchers)

    private fun map(estate: Estate?) : AOEAddressViewState? = if (estate?.address != null){
        AOEAddressViewState(number = estate.address.number,
            complement = estate.address.complement,
            street = estate.address.street,
            district = estate.address.district,
            postcode = estate.address.postCode,
            city = estate.address.city
        )
    }else{
        null
    }

    fun onSaveClick(){
        when {
            district.isBlank() -> {
                showInvalidInputMessage("District cannot be empty")
                return
            }
            city.isBlank() -> {
                showInvalidInputMessage("City cannot by empty")
                return
            }
            else -> createNewAddress()
        }
    }

    private fun createNewAddress() = viewModelScope.launch {
        val address = withContext(Dispatchers.Default) {
            Address(
                district,
                number,
                complement,
                street,
                postCode,
                city
            )
        }
        addRepo.setAddress(address)
        addEditAddressChannel.send(AddEditAddressEvent.NavigateWithResult(ADD_EDIT_NEXT_RESULT))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditAddressChannel.send(AddEditAddressEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditAddressEvent{
        data class ShowInvalidInputMessage(val msg: String) : AddEditAddressEvent()
        data class NavigateWithResult(val result: Int) : AddEditAddressEvent()
    }

}



