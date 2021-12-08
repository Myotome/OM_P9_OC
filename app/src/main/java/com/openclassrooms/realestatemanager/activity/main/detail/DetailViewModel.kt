package com.openclassrooms.realestatemanager.activity.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsforinstrutest.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    dataSourceRepository: DataSourceRepository
) :
    ViewModel() {

    @ExperimentalCoroutinesApi
    val detailLiveData = dataSourceRepository.getEstateById()!!.mapNotNull { estate ->
        map(estate)
    }.asLiveData(Dispatchers.IO)


    private fun map(estate: Estate): DetailViewState =
        DetailViewState(
            id = estate.id,
            type = estate.primaryEstateData.estateType,
            price = estate.primaryEstateData.price,
            surface = estate.primaryEstateData.surface,
            rooms = estate.primaryEstateData.rooms,
            bedrooms = estate.secondaryEstateData.bedroom,
            bathrooms = estate.secondaryEstateData.bathroom,
            description = estate.secondaryEstateData.description,
            landSize = estate.primaryEstateData.landSize,
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
            realtor = estate.secondaryEstateData.realtor ?: "",
            entryDate = Utils.getLongToString(estate.secondaryEstateData.entryDate!!),
            modificationDate = if (estate.secondaryEstateData.modificationDate != null) Utils.getLongToString(estate.secondaryEstateData.modificationDate) else "",
            soldDate = if (estate.primaryEstateData.soldDate != null) Utils.getLongToString(estate.primaryEstateData.soldDate) else "",
            onSale = estate.primaryEstateData.onSale,
            formattedAddress = getFormattedAddress(estate.address, estate.lat, estate.lng)

        )

    private fun getAddressString(address: Address): String {

        return ("${address.number}   ${address.complement ?: ""} \n" +
                "${address.street}\n" +
                "${address.district}\n" +
                address.city)
    }

    private fun getFormattedAddress(address: Address, lat: Double?, lng: Double?): String {
        val street = address.street.trim().replace(" ", "+")
        val city = address.city.trim().replace(" ", "+")
        return ("https://maps.googleapis.com/maps/api/" +
                "staticmap?size=200x200&scale=2" +
                "&center=${address.number}+$street+$city" +
                "&markers=color:blue|$lat,$lng" +
                "&key=${BuildConfig.MAPS_API_KEY}")
    }
}
