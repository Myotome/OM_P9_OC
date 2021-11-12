package com.openclassrooms.realestatemanager.activity.main.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ListViewModel @Inject constructor(private val roomRepo: RoomDatabaseRepository) :
    ViewModel() {

//    private val allEstate: LiveData<List<Estate>> = roomRepo.allProperty.asLiveData()

//    fun insertEstate(estate: Estate) = viewModelScope.launch { roomRepo.insertEstate(estate) }
    val isSearching = roomRepo.isSearching

    fun setCurrentEstateId(estateId: Int) = roomRepo.setCurrentEstateId(estateId)

//    val uiStateLiveData = roomRepo.allProperty.mapNotNull { estates ->
//        estates?.map { estate -> map(estate) }
//    }.asLiveData()

    val uiStateLiveData = roomRepo.querySearchFlow.mapNotNull { estates ->
//        Log.d(TAG, "uistate: ${estates?.size}")
        estates?.map{estate -> map(estate) }
    }.asLiveData()


    private fun map(estate: Estate): ListViewState =

        ListViewState(
            estate.id,
            estate.estateType,
            estate.address.district,
            estate.price,
            estate.onSale,
            estate.listPhoto[0]
        )

    fun clearSearch() {
//        roomRepo.searchQuery(null)
//        roomRepo.isSearching(false)
        roomRepo.setSearchQuery(null)
    }


}