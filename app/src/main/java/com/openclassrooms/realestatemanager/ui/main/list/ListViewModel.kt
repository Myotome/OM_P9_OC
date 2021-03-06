package com.openclassrooms.realestatemanager.ui.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject


/**
 * Main logic for fragment
 * Get data from repository and create view state to show correct data on view
 */

@ExperimentalCoroutinesApi
@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
    ) :
    ViewModel() {

    val isSearching = dataSourceRepository.isSearching

    fun setCurrentEstateId(estateId: Long) = dataSourceRepository.setCurrentEstateById(estateId)

    val uiStateLiveData = dataSourceRepository.querySearchFlow.mapNotNull { estates ->
        estates?.map{estate -> map(estate) }
    }.asLiveData()


    private fun map(estate: Estate): ListViewState {
        return ListViewState(
            estate.id,
            estate.primaryEstateData.estateType,
            estate.address.district,
            estate.primaryEstateData.price,
            estate.primaryEstateData.onSale,
            estate.listPhoto[0],
        )
    }

    fun clearSearch() {
        dataSourceRepository.setSearchQuery(null)
    }


}