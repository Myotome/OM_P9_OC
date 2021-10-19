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
    @TypeConverter
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun listPhotoToString (value: List<Photo>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun stringToListPhoto(value: String): List<Photo> {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(value, type)
    }
}
//    @TypeConverter
//    fun uriToStringForStorage(photos: List<Photo>?): String {
//        var string = ""
//        if (photos != null) {
//            for(photo in photos){
//                string += "${photo.location.toString()},"
//            }
//        }
//        return string
//        return photo?.listPhoto?.joinToString(",")
//        var string = ""
//        for(Uri in photo?.listUri!!){
//            string+= "$Uri,"
//        }

//    }

//    @TypeConverter
//    fun stringToUri(string: String): List<Photo>{
//        val list:MutableSet<Uri> = mutableSetOf(Uri.parse(string.split(",").toString()))
//        val listPhoto = ArrayList<Photo>()
//        for(i in 0 until list.size){
//            listPhoto.add(Photo("name", list.elementAt(i)))
//        }
//        return listPhoto
//    }


