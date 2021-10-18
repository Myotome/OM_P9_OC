package com.openclassrooms.realestatemanager.activity.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.CoroutineDispatchers
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) :
    ViewModel() {

    @FlowPreview
    val detailLiveData = roomRepo.estateById.mapNotNull { estate -> map(estate) }
        .asLiveData(coroutineDispatchers.ioDispatchers)

    private fun map(estate: Estate): DetailViewState =
        DetailViewState(
            type = estate.estateType,
            price = estate.price,
            surface = estate.surface,
            rooms = estate.room,
            bedrooms = estate.bedrooms,
            bathrooms = estate.bathrooms,
            description = estate.description,
            landSize = estate.landSize,
            school = estate.interest.school,
            store = estate.interest.store,
            park = estate.interest.park,
            restaurant = estate.interest.restaurant,
            movie = estate.interest.movie,
            theatre = estate.interest.theatre,
            subway = estate.interest.subway,
            nightlife = estate.interest.nightlife
        )
}
