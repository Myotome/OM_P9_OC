package com.openclassrooms.realestatemanager.database

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

    @Query("SELECT * FROM estate_table")
    fun getAllEstate(): Flow<List<Estate>?>

    @Query("SELECT * FROM estate_table WHERE estate_id =:id")
    fun getCurrentEstate(id: Int): Flow<Estate>

//    @RawQuery
//    fun getSearchEstate(query : SupportSQLiteQuery): Flow<List<Estate>?>
}