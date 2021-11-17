package com.openclassrooms.realestatemanager.activity.main

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
) : ViewModel() {

    fun clearCurrentEstate(){
        dataSourceRepository.setCurrentEstateById(null)
    }

    fun clearSearch() {
//        roomRepo.searchQuery(null)
//        roomDatabaseRepository.isSearching(false)
        dataSourceRepository.setSearchQuery(null)
    }
}