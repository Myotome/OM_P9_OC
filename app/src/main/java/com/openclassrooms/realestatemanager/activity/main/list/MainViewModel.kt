package com.openclassrooms.realestatemanager.activity.main.list

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class MainViewModel @Inject constructor(private val roomRepo: RoomDatabaseRepository) :
    ViewModel() {

//    private val allEstate: LiveData<List<Estate>> = roomRepo.allProperty.asLiveData()

//    fun insertEstate(estate: Estate) = viewModelScope.launch { roomRepo.insertEstate(estate) }

    fun isCurrentEstate(estateId: Int) = roomRepo.isCurrentEstate(estateId)

    val uiStateLiveData = roomRepo.allProperty.mapLatest { estates ->
        estates?.map { estate -> map(estate) }
    }.asLiveData()


    private fun map(estate: Estate): MainViewState =

            MainViewState(estate.id,
                estate.estateType,
                estate.address.district,
                estate.price,
                if(!estate.listPhoto.isNullOrEmpty()) estate.listPhoto[0] else null
            )




}