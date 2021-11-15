package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {


    val isSearching = MutableLiveData(false)

    fun isSearching(status: Boolean) {
        isSearching.value = status
    }

    private val querySearchMutableSharedFlow = MutableStateFlow<SimpleSQLiteQuery?>(null)
    val querySearchFlow = querySearchMutableSharedFlow.flatMapLatest { query ->
        if (query == null) {
            estateDAO.getAllEstate()
        } else {
            estateDAO.getSearchEstate(query)
        }
    }

    fun setSearchQuery(query: SimpleSQLiteQuery?) {
        querySearchMutableSharedFlow.value = query
    }


    private val currentEstateId = MutableLiveData<Int?>()
    fun setCurrentEstateId(estateId: Int?) {
        currentEstateId.value = estateId
    }

    fun getEstateById(): Flow<Estate>? {
        return if (currentEstateId.value != null) {
            estateDAO.getCurrentEstate(currentEstateId.value!!)
        } else {
            null
        }
    }


    suspend fun updateLatLngById(id: Int, lat: Double, lng: Double) {
        estateDAO.updateLatLngById(id, lat, lng)
    }
}


