package com.openclassrooms.realestatemanager.ui.addoredit.fragment.partone

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.PrimaryEstateData
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * Main logic for fragment
 * When edition, get data from repository and create view state to show correct data on view
 * Assert all obligatory fields are complete before save part
 * Flow channel to communicate with the view
 * Get sold date if needed
 */

@DelicateCoroutinesApi
@HiltViewModel
class AOEPartOneViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
): ViewModel() {

    private val addEditOneChannel = Channel<AddEditOneEvent>()
    val addEditOneEvent = addEditOneChannel.receiveAsFlow()

    var onSale = true
    var type: String? = null
    var price: Int? = null
    var surface: Double? = null
    var rooms: Int? = null
    var landsize: Double? = null
    private var soldTime: Long? = null


    val currentEstate = dataSourceRepository.getEstateById()?.mapNotNull { estate ->
        map(estate)
    }?.asLiveData(Dispatchers.IO)

    private fun map(estate: Estate?) : AOEPartOneViewState? = if (estate?.address != null){
        AOEPartOneViewState(
            onSale = estate.primaryEstateData.onSale,
            type = estate.primaryEstateData.estateType,
            price = estate.primaryEstateData.price,
            surface = estate.primaryEstateData.surface,
            room = estate.primaryEstateData.rooms,
            landSize = estate.primaryEstateData.landSize
        )
    }else{
        null
    }

    fun onSaveClick(){
        when{
            type.isNullOrBlank()-> {
                showInvalidInputMessage("Property type cannot be empty")
                return
            }
            (price == null) -> {
                showInvalidInputMessage("Price cannot be empty")
                return
            }
            (surface == null) ->{
                showInvalidInputMessage("Surface cannot be empty")
                return
            }
            else ->{
                createPartOne()

            }
        }
    }

    private fun createPartOne() = GlobalScope.launch{
        if(!onSale) soldTime = Utils.getLongFormatDate()
        dataSourceRepository.setPartOne(PrimaryEstateData( onSale, type!!, price!!, surface!!, rooms, landsize, soldTime))
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