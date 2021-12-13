package com.openclassrooms.realestatemanager.ui.main.maps

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.repository.LocationRepository
import com.openclassrooms.realestatemanager.repository.RetrofitRepository
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class MapsViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository,
    locationRepository: LocationRepository,
    private val retrofitRepository: RetrofitRepository
) :
    ViewModel() {

    private val currentPosition = locationRepository.getCurrentPosition()
    val isSearching = dataSourceRepository.isSearching


    private val estateLiveData = dataSourceRepository.querySearchFlow.asLiveData()

    private val mediator = MediatorLiveData<MapsPositionViewState>().apply {
        addSource(currentPosition) { position -> mediatorCombine(position, estateLiveData.value) }
        addSource(estateLiveData) { estate -> mediatorCombine(currentPosition.value, estate) }
    }

    fun assertAllEstateHadLatLng() = viewModelScope.launch(Dispatchers.IO) {
        estateLiveData.value?.forEach {
            if (it.lat == 0.0 && it.lng == 0.0 ) {
                val formatStreet = it.address.street.trim().replace(" ", "+")
                val formatCity = it.address.city.trim().replace(" ", "+")
                retrofitRepository.setGeocoding("${it.address.number}+${formatStreet}+${formatCity}")
                val modDate = Utils.getLongFormatDate()
                delay(1000)
                val geo = retrofitRepository.geocodingResponse.first().geometry.location
                dataSourceRepository.updateLatLngById(it.id, geo.lat, geo.lng, it.secondaryEstateData.firebaseId, modDate)
            }
        }
    }

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

    fun getViewState() = mediator

    fun isCurrentEstate(estateId: Long) = dataSourceRepository.setCurrentEstateById(estateId)

    fun clearSearch() = dataSourceRepository.setSearchQuery(null)

}