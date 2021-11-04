package com.openclassrooms.realestatemanager.activity.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ListViewModel @Inject constructor(private val roomRepo: RoomDatabaseRepository) :
    ViewModel() {

//    private val allEstate: LiveData<List<Estate>> = roomRepo.allProperty.asLiveData()

//    fun insertEstate(estate: Estate) = viewModelScope.launch { roomRepo.insertEstate(estate) }
    val isSearching = roomRepo.isSearching

    fun setCurrentEstateId(estateId: Int) = roomRepo.setCurrentEstateId(estateId)

    val uiStateLiveData = roomRepo.allProperty.mapLatest { estates ->
        estates?.map { estate -> map(estate) }
    }.asLiveData()


    private fun map(estate: Estate): ListViewState =

        ListViewState(
            estate.id,
            estate.estateType,
            estate.address.district,
            estate.price,
            estate.onSale,
            if (!estate.listPhoto.isNullOrEmpty()) estate.listPhoto[0] else null
        )

    fun clearSearch() {
        roomRepo.searchQuery(null)
        roomRepo.isSearching(false)
    }


}