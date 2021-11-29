package com.openclassrooms.realestatemanager.activity.main.list

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
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
            Uri.parse(estate.listPhoto[0].storageUriString)
//        null
        )
    }

    fun clearSearch() {
        dataSourceRepository.setSearchQuery(null)
    }


}