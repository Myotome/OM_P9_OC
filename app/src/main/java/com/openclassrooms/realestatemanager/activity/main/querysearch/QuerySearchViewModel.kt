package com.openclassrooms.realestatemanager.activity.main.querysearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.utils.CoroutineDispatchers
import com.openclassrooms.realestatemanager.repository.RoomDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuerySearchViewModel @Inject constructor(
    private val roomRepo: RoomDatabaseRepository,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    var onSale = true
    var sold = false
//    var type: String? = null
    var penthouse = false
    var house = false
    var loft = false
    var apartment = false
    var castle = false
    var mansion = false
    var distric: String? = null
    var priceMin: Int? = null
    var priceMax: Int? = null
    var surfaceMin: Double? = null
    var surfaceMax: Double? = null
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
    var entryDateCode: Int = 1
    var entryDateText: Long? = null
    var soldDateCode: Int = 0
    var soldDateText: Long? = null
    var photoMin: Int? = null
    var typeIsUsed = false
    var interestIsUsed = false


    fun search() {
        val typeList = listOf(penthouse, house, loft, apartment, castle, mansion)
        val interestList = listOf(school, store, park, restaurant, movie, theatre, subway, nightlife)
        var queryString = "SELECT * FROM estate_table"
        val args = ArrayList<Any>()
        var condition = false

        if ((onSale && !sold) || (!onSale && sold)) {
            Log.d(TAG, "search: sale : $onSale, sold : $sold")
            queryString += " WHERE"
            queryString += " onSale =?"
            condition = true
            when {
                onSale -> args.add(true)
                sold -> args.add(false)
            }
        }

        if(!condition && typeList.contains(true)){
            queryString += " WHERE("
            condition = true
            typeIsUsed = true
        }else if(condition && typeList.contains(true)) {
            queryString += " AND("
            typeIsUsed = true
        }
        var booleanTypeCondition = false
        for ((index, value) in typeList.withIndex()) {
            if (value) {
                if (booleanTypeCondition) queryString += " OR" else booleanTypeCondition = true
                when (index) {
                    0 -> {queryString += " estateType=?"; args.add("Penthouse")}
                    1 -> {queryString += " estateType=?"; args.add("House")}
                    2 -> {queryString += " estateType=?"; args.add("Loft")}
                    3 -> {queryString += " estateType=?"; args.add("Apartment")}
                    4 -> {queryString += " estateType=?"; args.add("Castle")}
                    5 -> {queryString += " estateType=?"; args.add("Mansion")}
                }
            }
        }
        if(typeIsUsed)queryString += ")"

//        if (type != null) {
//            queryString += if (condition) " AND" else " WHERE"; condition = true
//            queryString += " estateType =?"
//            args.add(type!!)
//        }

        if (distric != null){
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " district LIKE '%' || ? || '%'"
            args.add(distric!!)
        }

        if (priceMin != null && priceMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " price BETWEEN ? AND ?"
            args.add(priceMin!!)
            args.add(priceMax!!)
        } else if (priceMin != null && priceMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " price >=?"
            args.add(priceMin!!)
        } else if (priceMin == null && priceMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " price <=?"
            args.add(priceMax!!)
        }

        if (surfaceMin != null && surfaceMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " surface BETWEEN ? AND ?"
            args.add(surfaceMin!!)
            args.add(surfaceMax!!)
        } else if (surfaceMin != null && surfaceMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " surface >=?"
            args.add(surfaceMin!!)
        } else if (surfaceMin == null && surfaceMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " surface <=?"
            args.add(surfaceMax!!)
        }

        if (roomMin != null && roomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " room BETWEEN ? AND ?"
            args.add(roomMin!!)
            args.add(roomMax!!)
        } else if (roomMin != null && roomMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " room >=?"
            args.add(roomMin!!)
        } else if (roomMin == null && roomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " room <=?"
            args.add(roomMax!!)
        }

        if (bedroomMin != null && bedroomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bedrooms BETWEEN ? AND ?"
            args.add(bedroomMin!!)
            args.add(bedroomMax!!)
        } else if (bedroomMin != null && bedroomMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bedrooms >=?"
            args.add(bedroomMin!!)
        } else if (bedroomMin == null && bedroomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bedrooms <=?"
            args.add(bedroomMax!!)
        }

        if (bathroomMin != null && bathroomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bathrooms BETWEEN ? AND ?"
            args.add(bathroomMin!!)
            args.add(bathroomMax!!)
        } else if (bathroomMin != null && bathroomMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bathrooms >=?"
            args.add(bathroomMin!!)
        } else if (bathroomMin == null && bathroomMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " bathrooms <=?"
            args.add(bathroomMax!!)
        }

        if (landSizeMin != null && landSizeMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " landSize BETWEEN ? AND ?"
            args.add(landSizeMin!!)
            args.add(landSizeMax!!)
        } else if (landSizeMin != null && landSizeMax == null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " landSize >=?"
            args.add(landSizeMin!!)
        } else if (landSizeMin == null && landSizeMax != null) {
            queryString += if (condition) " AND" else " WHERE"; condition = true
            queryString += " landSize <=?"
            args.add(landSizeMax!!)
        }

        if(!condition && interestList.contains(true)){
            queryString += " WHERE("
            condition = true
            interestIsUsed = true
        }else if(condition && interestList.contains(true)) {
            queryString += " AND("
            interestIsUsed = true
        }
            var booleanCondition = false
            for ((index, value) in interestList.withIndex()) {
                if (value) {
                    if (booleanCondition) queryString += " OR" else booleanCondition = true
                    when (index) {
                        0 -> {queryString += " school=?"; args.add(school)}
                        1 -> {queryString += " store=?"; args.add(store)}
                        2 -> {queryString += " park=?"; args.add((park))}
                        3 -> {queryString += " restaurant=?"; args.add(restaurant)}
                        4 -> {queryString += " movie=?"; args.add(movie)}
                        5 -> {queryString += " theatre=?"; args.add(theatre)}
                        6 -> {queryString += " subway=?"; args.add(subway)}
                        7 -> {queryString += " nightlife=?"; args.add(nightlife)}
                    }
                }
            }
            if(interestIsUsed)queryString += ")"

        if(entryDateText!=null){
            queryString += if (condition) " AND" else " WHERE"; condition = true
            when(entryDateCode){
                0-> {queryString += "  entryDate <=?"; args.add(entryDateText!!)}
                1->{queryString += " entryDate >=?"; args.add(entryDateText!!)}
            }
        }
        if(soldDateText!=null){
            queryString += if (condition) " AND" else " WHERE"; condition = true
            when(soldDateCode){
                0-> {queryString += "  soldDate <=?"; args.add(soldDateText!!)}
                1->{queryString += " soldDate >=?"; args.add(soldDateText!!)}
            }
        }

        if(photoMin!=null){
            queryString += if (condition) " AND" else " WHERE"
            queryString += " COUNT (listPhoto) >=?"
            args.add(photoMin!!)
        }

        //End of query string
        queryString += ";"
        Log.d("DEBUGKEY", "search: queryString : $queryString")
        val query = SimpleSQLiteQuery(queryString, args.toArray())
        setSearch(query)

    }

    private fun setSearch(query: SimpleSQLiteQuery) {
//        roomRepo.searchQuery(query)
        roomRepo.setSearchQuery(query)
        roomRepo.isSearching(true)
    }
}