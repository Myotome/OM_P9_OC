package com.openclassrooms.realestatemanager.repository

import android.util.Log
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.model.geocodingPOJO.GeocodingPOJO
import com.openclassrooms.realestatemanager.model.geocodingPOJO.Result
import com.openclassrooms.realestatemanager.utils.GoogleApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitRepository @Inject constructor() {

    private val googleApi: GoogleApiService
    private val geocodingResponseMutableSharedFlow = MutableSharedFlow<Result>(1)
    val geocodingResponse = geocodingResponseMutableSharedFlow.asSharedFlow()

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        googleApi = retrofit.create(GoogleApiService::class.java)
    }

    suspend fun setGeocoding(address: String){
        Log.d(TAG, "setGeocoding: address $address")
        val geocodingPOJOResponse = googleApi.getGeocoding(address, BuildConfig.MAPS_API_KEY)
        for (i in geocodingPOJOResponse.results.indices){
            geocodingResponseMutableSharedFlow.tryEmit(geocodingPOJOResponse.results[i])
            Log.d(TAG, "setGeocoding: tryemit lat value : ${geocodingResponseMutableSharedFlow.first().geometry.location.lat}\n"
            +" setGeocoding: geocodingresponse lat : ${geocodingResponse.first().geometry.location.lat}")
        }

    }

}