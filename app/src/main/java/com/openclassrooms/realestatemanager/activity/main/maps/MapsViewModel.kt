package com.openclassrooms.realestatemanager.activity.main.maps

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.repository.LocationRepository
import com.openclassrooms.realestatemanager.repository.RetrofitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val roomRepo: DataSourceRepository,
    locationRepository: LocationRepository,
    private val retrofitRepository: RetrofitRepository
) :
    ViewModel() {

    private val currentPosition = locationRepository.getCurrentPosition()
    val isSearching = roomRepo.isSearching

    @FlowPreview
//    private val estateLiveData = roomRepo.allProperty.asLiveData()
    private val estateLiveData = roomRepo.querySearchFlow.asLiveData()

    @FlowPreview
    private val mediator = MediatorLiveData<MapsPositionViewState>().apply {
        addSource(currentPosition) { position -> mediatorCombine(position, estateLiveData.value) }
        addSource(estateLiveData) { estate -> mediatorCombine(currentPosition.value, estate) }
    }

    @FlowPreview
    fun assertAllEstateHadLatLng() = viewModelScope.launch {
        estateLiveData.value?.forEach {
            if (it.lat == 0.0 && it.lng == 0.0) {
                val formatStreet = it.address.street.trim().replace(" ", "+")
                val formatCity = it.address.city.trim().replace(" ", "+")
                retrofitRepository.setGeocoding("${it.address.number}+${formatStreet}+${formatCity}")
                delay(1000)
                val geo = retrofitRepository.geocodingResponse.first().geometry.location
                roomRepo.updateLatLngById(it.id, geo.lat, geo.lng)
            }
        }
    }

    @FlowPreview
    private fun mediatorCombine(position: LatLng?, listEstate: List<Estate>?) {

        val listViewState = ArrayList<MapsEstateViewState>()
        if (listEstate != null) {
            for (estate in listEstate) {
                listViewState.add(MapsEstateViewState(estate.id, estate.lat, estate.lng))
            }
        }
        if (position != null) {
            mediator.value =
                MapsPositionViewState(position.latitude, position.longitude, listViewState)
        }
    }

    @FlowPreview
    fun getViewState() = mediator

    fun isCurrentEstate(estateId: Long) = roomRepo.setCurrentEstateById(estateId)

    fun clearSearch(){
        roomRepo.apply {
            setSearchQuery(null)
        }
    }
}