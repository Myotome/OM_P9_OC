package com.openclassrooms.realestatemanager.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
) : ViewModel() {

    fun clearCurrentEstate() = dataSourceRepository.setCurrentEstateById(null)

    fun clearSearch() = dataSourceRepository.setSearchQuery(null)


//    fun getEstateFromFirestore() = viewModelScope.launch {
//        val firestoreListEstates = dataSourceRepository.getAllEstateFromFirestore.mapNotNull { firestoreEstate ->
//            firestoreEstate?.map{estate ->  }
//        }
//        val roomEstate = dataSourceRepository.querySearchFlow
//
//
//    }
}