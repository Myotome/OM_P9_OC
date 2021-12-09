package com.openclassrooms.realestatemanager.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.model.Estate

@Database(entities = [Estate::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class EstateRoomDatabase : RoomDatabase() {

    abstract fun EstateDAO(): EstateDAO

}