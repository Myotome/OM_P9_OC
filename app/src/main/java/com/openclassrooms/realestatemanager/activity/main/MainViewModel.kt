package com.openclassrooms.realestatemanager.activity.main

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomDatabaseRepository: RoomDatabaseRepository
) : ViewModel() {

    fun clearCurrentEstate(){
        roomDatabaseRepository.setCurrentEstateId(-1)
    }

    fun clearSearch() {
//        roomRepo.searchQuery(null)
//        roomDatabaseRepository.isSearching(false)
        roomDatabaseRepository.setSearchQuery(null)
    }
}