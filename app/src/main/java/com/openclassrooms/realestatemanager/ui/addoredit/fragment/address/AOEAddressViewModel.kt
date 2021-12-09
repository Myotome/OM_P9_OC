package com.openclassrooms.realestatemanager.ui.addoredit.fragment.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.repository.RetrofitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AOEAddressViewModel @Inject constructor(
//    private val addRepo: AddRepository,
    private val dataSourceRepository: DataSourceRepository,
    private val retrofitRepository: RetrofitRepository,
//    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditAddressChannel = Channel<AddEditAddressEvent>()
    val addEditAddressEvent = addEditAddressChannel.receiveAsFlow()

    var district: String = ""
    var number: Int? = null
    var complement: String? = null
    var street: String = ""
    var city: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var internetAvailable = false

    val currentEstate = dataSourceRepository.getEstateById()?.mapNotNull { estate ->
        map(estate)
    }?.asLiveData(Dispatchers.IO)

    private fun map(estate: Estate?): AOEAddressViewState? = if (estate?.address != null) {
        AOEAddressViewState(
            number = estate.address.number,
            complement = estate.address.complement,
            street = estate.address.street,
            district = estate.address.district,
            city = estate.address.city,
            lat = estate.lat,
            lng = estate.lng
        )
    } else {
        null
    }

    fun onSaveClick() {
        when {
            number == null -> {
                showInvalidInputMessage("Number cannot be empty")
                return
            }
            street.isBlank() -> {
                showInvalidInputMessage("Street cannot be empty")
                return
            }
            district.isBlank() -> {
                showInvalidInputMessage("District cannot be empty")
                return
            }
            city.isBlank() -> {
                showInvalidInputMessage("City cannot by empty")
                return
            }

            else -> {
                if (internetAvailable && lat == 0.0 && lng == 0.0) setGeocoding()
                createNewAddress()
            }
        }
    }

    private fun setGeocoding() = GlobalScope.launch {
        val formatStreet = street.trim().replace(" ", "+")
        val formatCity = city.trim().replace(" ", "+")
        retrofitRepository.setGeocoding("${number}+${formatStreet}+${formatCity}")
    }

    private suspend fun getGeocoding() {
        val geo = retrofitRepository.geocodingResponse.first()
        lat = geo.geometry.location.lat
        lng = geo.geometry.location.lng
    }

    private fun createNewAddress() = viewModelScope.launch {
        if (internetAvailable && lat == 0.0 && lng == 0.0) getGeocoding()
        val address = withContext(Dispatchers.Default) {
            Address(
                district,
                number!!,
                complement,
                street,
                city
            )
        }
        dataSourceRepository.setAddress(address, lat, lng)
        addEditAddressChannel.send(AddEditAddressEvent.NavigateWithResult(ADD_EDIT_NEXT_RESULT))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditAddressChannel.send(AddEditAddressEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditAddressEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditAddressEvent()
        data class NavigateWithResult(val result: Int) : AddEditAddressEvent()
    }

}



