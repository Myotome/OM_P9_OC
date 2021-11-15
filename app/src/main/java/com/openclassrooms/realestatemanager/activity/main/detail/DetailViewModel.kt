package com.openclassrooms.realestatemanager.activity.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) :
    ViewModel() {

    @ExperimentalCoroutinesApi
    val detailLiveData = roomRepo.getEstateById()!!.mapNotNull { estate ->
        map(estate)
    }.asLiveData(coroutineDispatchers.ioDispatchers)
    

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
        nightlife = estate.interest.nightlife,
        photoList = estate.listPhoto,
        address = getAddressString(estate.address),
        realtor = estate.realtor ?: "",
        entryDate = Utils.getLongToString(estate.entryDate),
        modificationDate = if (estate.modificationDate != null) Utils.getLongToString(estate.modificationDate) else "",
        soldDate = if (estate.soldDate != null) Utils.getLongToString(estate.soldDate) else "",
        onSale = estate.onSale,
        formattedAddress = getFormattedAddress(estate.address, estate.lat, estate.lng)

    )

private fun getAddressString(address: Address): String {

    return ("${address.number}   ${address.complement?:""} \n" +
            "${address.street}\n" +
            "${address.district}\n" +
            address.city)
}

private fun getFormattedAddress(address: Address, lat:Double?, lng:Double? ): String {
    val street = address.street.trim().replace(" ", "+")
    val city = address.city.trim().replace(" ", "+")
    return ("https://maps.googleapis.com/maps/api/" +
            "staticmap?size=200x200&scale=2" +
            "&center=${address.number}+$street+$city" +
            "&markers=color:blue|$lat,$lng" +
            "&key=${BuildConfig.MAPS_API_KEY}")
}
}
