package com.openclassrooms.realestatemanager.activity.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ListViewModel @Inject constructor(
    private val roomRepo: DataSourceRepository
    ) :
    ViewModel() {

    val isSearching = roomRepo.isSearching

    fun setCurrentEstateId(estateId: Long) = roomRepo.setCurrentEstateById(estateId)

    val uiStateLiveData = roomRepo.querySearchFlow.mapNotNull { estates ->
        estates?.map{estate -> map(estate) }
    }.asLiveData()


    private fun map(estate: Estate): ListViewState =

        ListViewState(
            estate.id,
            estate.primaryEstateData.estateType,
            estate.address.district,
            estate.primaryEstateData.price,
            estate.primaryEstateData.onSale,
            estate.listPhoto[0]
        )

    fun clearSearch() {
        roomRepo.setSearchQuery(null)
    }


}