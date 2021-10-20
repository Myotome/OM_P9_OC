package com.openclassrooms.realestatemanager.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.model.Photo
import java.io.ByteArrayOutputStream

class Converters {
//    @TypeConverter
//    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return outputStream.toByteArray()
//    }
//
//    @TypeConverter
//    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }

    @TypeConverter
    fun listPhotoToString (value: List<Photo>?): String? {
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

    @TypeConverter
    fun photoToString (photo: Photo?) : String?{
        val type = object : TypeToken<Photo>(){}.type
        return Gson().toJson(photo, type)
    }

    @TypeConverter
    fun stringToPhoto(string: String?) : Photo?{
        val type = object : TypeToken<Photo>(){}.type
        return Gson().fromJson<Photo>(string, type)
    }
}
    @TypeConverter
    fun uriToString(uri: Uri?): String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(string: String?): Uri? {
       return if(string == null) null else Uri.parse(string)
    }


