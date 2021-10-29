package com.openclassrooms.realestatemanager.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepository @Inject constructor(private val estateDAO: EstateDAO) {

    private val setCurrentId = MutableLiveData<Int>()
    private val setQuerySearch = MutableLiveData<SimpleSQLiteQuery?>(null)

    @FlowPreview
    val estateById = setCurrentId.asFlow().flatMapConcat { id -> estateDAO.getCurrentEstate(id) }

    @FlowPreview
    val allProperty = setQuerySearch.asFlow().flatMapMerge { query -> estateDAO.getEstate(query) }

    fun isCurrentEstate(estateId: Int) {
        setCurrentId.value = estateId
    }

    fun searchQuery(query: SimpleSQLiteQuery?) {
        setQuerySearch.value = query
    }
}


