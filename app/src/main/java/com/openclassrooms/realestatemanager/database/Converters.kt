package com.openclassrooms.realestatemanager.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.model.Photo
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun uriToStringForStorage(photos: List<Photo>?): String {
        var string = ""
        if (photos != null) {
            for(photo in photos){
                string += "${photo.location.toString()},"
            }
        }
        return string
//        return photo?.listPhoto?.joinToString(",")
//        var string = ""
//        for(Uri in photo?.listUri!!){
//            string+= "$Uri,"
//        }

    }

    @TypeConverter
    fun stringToUri(string: String): List<Photo>{
        val list:MutableSet<Uri> = mutableSetOf(Uri.parse(string.split(",").toString()))
        val listPhoto = ArrayList<Photo>()
        for(i in 0 until list.size){
            listPhoto.add(Photo("name", list.elementAt(i)))
        }
        return listPhoto
    }

//    @TypeConverter
//    fun bitmapToString(bitmap: Bitmap): String {
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val byte = baos.toByteArray()
//        return Base64.encodeToString(byte, Base64.DEFAULT)
//    }
//
//    @TypeConverter
//    fun stringToBitmap(encodedString: String): Bitmap? {
//        return try {
//            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
//            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
//        }catch (e: Exception){
//            e.message
//            null
//        }
//    }
}