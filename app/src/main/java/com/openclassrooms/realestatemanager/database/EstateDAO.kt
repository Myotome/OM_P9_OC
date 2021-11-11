package com.openclassrooms.realestatemanager.database

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEstate(estate: Estate)

    @Update
    suspend fun updateEstate(estate: Estate)

    @Query("UPDATE estate_table SET lat=:lat, lng=:lng WHERE estate_id=:id")
    suspend fun updateLatLngById(id: Int, lat: Double, lng: Double)

    @Query("SELECT * FROM estate_table")
    fun getAllEstate(): Flow<List<Estate>?>

    @Query("SELECT * FROM estate_table WHERE estate_id =:id")
    fun getCurrentEstate(id: Int): Flow<Estate>

    @RawQuery(observedEntities = [Estate::class])
    fun getSearchEstate(query : SupportSQLiteQuery): Flow<List<Estate>?>

    fun getEstate(query : SupportSQLiteQuery?) =
        when(query){
            null -> getAllEstate()
            else -> getSearchEstate(query)
        }

    /**
     * Usefully for content provider
     */
    @Query("SELECT * FROM estate_table" )
    fun findAllEstate(): Cursor
}