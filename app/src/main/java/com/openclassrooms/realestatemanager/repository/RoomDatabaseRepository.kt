package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {


    //    private val setQuerySearch = MutableLiveData<SimpleSQLiteQuery?>(null)
    val isSearching = MutableLiveData(false)


//    @FlowPreview
//    val allProperty = setQuerySearch.asFlow().flatMapMerge { query -> estateDAO.getEstate(query) }


//    fun searchQuery(query: SimpleSQLiteQuery?) {
//        setQuerySearch.value = query
//    }

//    fun isSearching(status: Boolean){
//        isSearching.value = status
//    }

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


    private val currentEstateIdMutableSharedFlow = MutableSharedFlow<Int>(replay = 1)
    val currentEstateIdFlow = currentEstateIdMutableSharedFlow.asSharedFlow()

    fun setCurrentEstateId(estateId: Int) {
        currentEstateIdMutableSharedFlow.tryEmit(estateId)
    }

    fun getEstateById(estateId: Int) = estateDAO.getCurrentEstate(estateId)


    suspend fun updateLatLngById(id: Int, lat: Double, lng: Double) {
        estateDAO.updateLatLngById(id, lat, lng)
    }
}


