package com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AOEPartTwoViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel(){

    private val addEditTwoChannel = Channel<AddEditTwoEvent>()
    val addEditTwoEvent = addEditTwoChannel.receiveAsFlow()

    var estateId: Int? = null
    var bedrooms: Int? = null
    var bathrooms: Int? = null
    var description: String? = null
    var realtor: String? = null
    var entryDate: Long? = null
    var modificationDate: Long? = null


    val currentEstate = roomRepo.getEstateById()?.mapNotNull { estate ->
        map(estate)
    }?.asLiveData(coroutineDispatchers.ioDispatchers)

    private fun map(estate: Estate?) : AOEPartTwoViewState? = if (estate?.address != null){
        AOEPartTwoViewState(id = estate.id,
            bedroom = estate.bedrooms,
            bathroom = estate.bathrooms,
            description = estate.description,
            realtor= estate.realtor,
            dateEntry = estate.entryDate
        )
    }else{
        null
    }

    fun onSaveClick() = viewModelScope.launch {
        if(entryDate == null) entryDate = Utils.getLongFormatDate() else modificationDate = Utils.getLongFormatDate()
        addRepo.setPartTwo(estateId, bedrooms, bathrooms, description, realtor, entryDate, modificationDate)
        addEditTwoChannel.send(AddEditTwoEvent.NavigateResult(ADD_EDIT_NEXT_RESULT))
    }

    sealed class AddEditTwoEvent{
        data class NavigateResult(val result: Int): AddEditTwoEvent()
    }
}