package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {

    val allProperty: Flow<List<Estate>?> = estateDAO.getAllEstate()

    private val setCurrentId = MutableLiveData<Int>()

    @FlowPreview
    val estateById = setCurrentId.asFlow().flatMapConcat { id -> estateDAO.getCurrentEstate(id) }

    fun isCurrentEstate(estateId: Int) {
        setCurrentId.value = estateId
    }

//    fun searchQuery(query: SimpleSQLiteQuery){
//        estateDAO.getSearchEstate(query = query)
//    }
}


