package com.openclassrooms.realestatemanager.activity.main.querysearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.CoroutineDispatchers
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class QuerySearchViewModel @Inject constructor(
    private val roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
): ViewModel() {

    var onSale = true
    var sold = false
    var type: String? = null
    var priceMin: Int? = null
    var priceMax: Int? = null
    var surfaceMin: Double? = null
    var surfaceMax:Double? = null
    var roomMin: Int? = null
    var roomMax: Int? = null
    var bedroomMin: Int? = null
    var bedroomMax: Int? = null
    var bathroomMin: Int? = null
    var bathroomMax: Int? = null
    var landSizeMin: Double? = null
    var landSizeMax: Double? = null
    var school = false
    var store = false
    var park = false
    var restaurant = false
    var movie = false
    var theatre = false
    var subway = false
    var nightlife = false
    var entryDateCode: Int = 2
    var entryDateText: String? = null
    var soldDateCode: Int = 0
    var soldDateText: String? = null
    var photoMin: Int? = null


    fun search(){
        var queryString = "SELECT * FROM estate_table"
        val args = ArrayList<Any>()
        var condition = false

        if(onSale) {
            queryString += " WHERE"
            queryString += " onSale = 0"
            args.add(onSale)
            condition = true
        }
        if(sold){
            queryString += " WHERE"
            queryString += " onSale = 1"
            args.add(sold)
            condition = true
        }

        val query = SimpleSQLiteQuery(queryString, args.toArray())
        setSearch(query)

    }

    private fun setSearch(query : SimpleSQLiteQuery) {
        Log.d("DEBUGKEY", "setSearch: $query")
//        roomRepo.searchQuery(query)
    }
}