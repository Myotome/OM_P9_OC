package com.openclassrooms.realestatemanager.activity.main.maps

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.LocationRepository
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val roomRepo: RoomDatabaseRepository,
    private val locationRepository: LocationRepository
) :
    ViewModel() {

//    @FlowPreview
//    val estateLivData = roomRepo.allProperty.mapNotNull { estate -> map(estate) }.asLiveData()

    private val currentPosition = locationRepository.getCurrentPosition()
    @FlowPreview
    private val estateLiveData = roomRepo.allProperty.asLiveData()
    @FlowPreview
    private val mediator = MediatorLiveData<MapsPositionViewState>().apply {
        addSource(currentPosition) {position -> mediatorCombine(position, estateLiveData.value)}
        addSource(estateLiveData) {estate -> mediatorCombine(currentPosition.value, estate)}
    }

    @FlowPreview
    private fun mediatorCombine(position: LatLng?, listEstate: List<Estate>?) {
        Log.d(TAG, "mediatorCombine: is call")
        val listViewState = ArrayList<MapsEstateViewState>()
        if (listEstate != null) {
            for (estate in listEstate) {
                listViewState.add(MapsEstateViewState(estate.id, estate.lat, estate.lng))
                Log.d(TAG,"mediatorCombine: list id : ${estate.id} listLat : ${estate.lat} listLng : ${estate.lng}")
            }
        }
        if (position != null) {
            mediator.value =
                MapsPositionViewState(position.latitude, position.longitude, listViewState)
            Log.d(TAG, "mediatorCombine: mapsPosition lat : ${position.latitude} lng : ${position.longitude} listSize : ${listViewState.size}")
        }
    }

    fun getViewState() = mediator

//    private fun map(listEstate: List<Estate>?): List<MapsEstateViewState> {
//        val listViewState = ArrayList<MapsEstateViewState>()
//        if (listEstate != null) {
//            for (estate in listEstate)
//                listViewState.add(MapsEstateViewState(estate.id, estate.lat, estate.lng))
//        }
//        return listViewState
//    }

    fun isCurrentEstate(estateId: Int) = roomRepo.setCurrentEstateId(estateId)
}