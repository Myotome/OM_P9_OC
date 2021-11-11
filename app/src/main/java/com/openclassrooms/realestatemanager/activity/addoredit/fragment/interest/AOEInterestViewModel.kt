package com.openclassrooms.realestatemanager.activity.addoredit.fragment.interest

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_NEXT_RESULT
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Interest
import com.openclassrooms.realestatemanager.repository.AddRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AOEInterestViewModel @Inject constructor(
    private val addRepo: AddRepository,
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val addEditInterestChannel = Channel<AddEditInterestEvent>()
    val addEditAddressEvent = addEditInterestChannel.receiveAsFlow()

    var school = false
    var store = false
    var park = false
    var restaurant = false
    var movie = false
    var theatre = false
    var subway = false
    var nightlife = false


    val currentEstate = roomRepo.currentEstateIdFlow.flatMapLatest { estateId ->
        roomRepo.getEstateById(estateId)
    }.mapNotNull { estate ->
        map(estate)
    }.asLiveData(coroutineDispatchers.ioDispatchers)

    private fun map(estate: Estate?) : AOEInterestViewState? = if (estate?.address != null){
        AOEInterestViewState(school = estate.interest.school,
            store = estate.interest.store,
            park = estate.interest.park,
            restaurant = estate.interest.restaurant,
            movie= estate.interest.movie,
            theatre= estate.interest.theatre,
            subway = estate.interest.subway,
            nightlife = estate.interest.nightlife
        )
    }else{
        null
    }

    fun onSaveClick() = viewModelScope.launch {
        val interest = withContext(Dispatchers.Default) {
            Interest(
                school = school,
                store = store,
                park = park,
                restaurant = restaurant,
                movie = movie,
                theatre = theatre,
                subway = subway,
                nightlife = nightlife
            )
        }
        addRepo.setInterest(interest)
        addEditInterestChannel.send(AddEditInterestEvent.NavigateWithResult(ADD_EDIT_NEXT_RESULT))

    }

    sealed class AddEditInterestEvent {
        data class NavigateWithResult(val result: Int) : AddEditInterestEvent()
    }
}
