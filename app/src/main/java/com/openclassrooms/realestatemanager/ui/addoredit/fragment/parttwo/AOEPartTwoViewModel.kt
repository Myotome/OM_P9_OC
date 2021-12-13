package com.openclassrooms.realestatemanager.ui.addoredit.fragment.parttwo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.SecondaryEstateData
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@DelicateCoroutinesApi
@HiltViewModel
class AOEPartTwoViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
) : ViewModel() {

    private val addEditTwoChannel = Channel<AddEditTwoEvent>()
    val addEditTwoEvent = addEditTwoChannel.receiveAsFlow()

    var estateId: Long? = null
    var firestoreId: String? = null
    var bedrooms: Int? = null
    var bathrooms: Int? = null
    var description: String? = null
    var realtor: String? = null
    var entryDate: Long? = null
    var modificationDate: Long? = null


    val currentEstate = dataSourceRepository.getEstateById()?.mapNotNull { estate ->
        map(estate)
    }?.asLiveData(Dispatchers.IO)

    private fun map(estate: Estate?): AOEPartTwoViewState? = if (estate?.address != null) {
        AOEPartTwoViewState(
            id = estate.id,
            firestoreId = estate.secondaryEstateData.firebaseId,
            bedroom = estate.secondaryEstateData.bedroom,
            bathroom = estate.secondaryEstateData.bathroom,
            description = estate.secondaryEstateData.description,
            realtor = estate.secondaryEstateData.realtor,
            dateEntry = estate.secondaryEstateData.entryDate!!
        )
    } else {
        null
    }

    fun onSaveClick() = GlobalScope.launch {
        if (entryDate == null) entryDate = Utils.getLongFormatDate() else modificationDate =
            Utils.getLongFormatDate()
        if (firestoreId == null) firestoreId = UUID.randomUUID().toString()
        dataSourceRepository.setPartTwo(
            SecondaryEstateData(
                firestoreId!!,
                bedrooms,
                bathrooms,
                description,
                realtor,
                entryDate,
                modificationDate
            ), estateId
        )
        addEditTwoChannel.send(AddEditTwoEvent.NavigateResult(ADD_EDIT_NEXT_RESULT))
    }

    sealed class AddEditTwoEvent {
        data class NavigateResult(val result: Int) : AddEditTwoEvent()
    }
}