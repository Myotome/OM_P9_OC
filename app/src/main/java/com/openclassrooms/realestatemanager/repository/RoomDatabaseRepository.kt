package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {

//    private val setCurrentId = MutableLiveData<Int>()
    private val setQuerySearch = MutableLiveData<SimpleSQLiteQuery?>(null)
    val isSearching = MutableLiveData(false)

//    @FlowPreview
//    val estateById = setCurrentId.asFlow().flatMapConcat { id -> estateDAO.getCurrentEstate(id) }

    @FlowPreview
    val allProperty = setQuerySearch.asFlow().flatMapMerge { query -> estateDAO.getEstate(query) }

//    fun setCurrentEstateId(estateId: Int) {
//        setCurrentId.value = estateId
//    }

    fun searchQuery(query: SimpleSQLiteQuery?) {
        setQuerySearch.value = query
    }

    fun isSearching(status: Boolean){
        isSearching.value = status
    }

    private val currentEstateIdMutableSharedFlow = MutableSharedFlow<Int>(replay = 1)
    val currentEstateIdFlow  = currentEstateIdMutableSharedFlow.asSharedFlow()

    fun setCurrentEstateId(estateId: Int) {
        currentEstateIdMutableSharedFlow.tryEmit(estateId)
    }
    fun getEstateById(estateId: Int) = estateDAO.getCurrentEstate(estateId)

    suspend fun updateLatLngById(id: Int, lat: Double, lng: Double) {
        estateDAO.updateLatLngById(id, lat, lng)
    }
}


