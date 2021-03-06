package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.geocodingPOJO.GeocodingPOJO
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Use with retrofit on RetrofitRepository
 */

interface GoogleApiService {

    @GET("geocode/json?")
    suspend fun getGeocoding(@Query ("address") address: String, @Query("key") googleKey: String): GeocodingPOJO
}