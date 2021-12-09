package com.openclassrooms.realestatemanager.database


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Photo

class Converters {


    @TypeConverter
    fun listPhotoToString(value: List<Photo>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun stringToListPhoto(value: String?): List<Photo>? {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(value, type)
    }

}


