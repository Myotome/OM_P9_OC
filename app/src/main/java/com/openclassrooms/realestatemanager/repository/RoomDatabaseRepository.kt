package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {


    val isSearching = MutableLiveData(false)

    private val querySearchMutableStateFlow = MutableStateFlow<SimpleSQLiteQuery?>(null)
    val querySearchFlow = querySearchMutableStateFlow.asSharedFlow().flatMapLatest { query ->
        if (query != null) {
            estateDAO.getSearchEstate(query)
        } else {
            estateDAO.getAllEstate()
        }
    }
    fun setSearchQuery(query: SimpleSQLiteQuery?, searchStatus: Boolean = false) {
        querySearchMutableStateFlow.tryEmit(query)
        isSearching.value = searchStatus
    }

    private val currentEstateId = MutableLiveData<Int?>(null)

    fun getEstateById(): Flow<Estate>? {
        return if(currentEstateId.value != null){
             estateDAO.getCurrentEstate(currentEstateId.value!!)
        }else {
            null
        }
    }
    fun setCurrentEstateById(estateId: Int?) {
        currentEstateId.value = estateId
    }

    suspend fun updateLatLngById(id: Int, lat: Double, lng: Double) {
        estateDAO.updateLatLngById(id, lat, lng)
    }
}


