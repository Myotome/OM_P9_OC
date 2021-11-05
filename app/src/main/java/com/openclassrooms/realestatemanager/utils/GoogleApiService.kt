package com.openclassrooms.realestatemanager.utils



import com.openclassrooms.realestatemanager.model.geocodingPOJO.GeocodingPOJO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleApiService {

    @GET("geocode/json?")
    suspend fun getGeocoding(@Query ("address") address: String, @Query("key") googleKey: String): GeocodingPOJO
}