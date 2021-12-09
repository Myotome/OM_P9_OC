package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.EstateDAO
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class EstateContentProvider : ContentProvider() {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface EstateContentProviderDao {
        fun contentProviderDao(): EstateDAO
    }

    companion object {
        private const val AUTHORITY =
            "com.openclassrooms.realestatemanager.provider/EstateContentProvider"
        private const val TABLE_NAME = "estate_table"
        val URI_MENU: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }


    override fun onCreate() = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val app = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(app, EstateContentProviderDao::class.java)
        val dao = hiltEntryPoint.contentProviderDao().findAllEstate()

        val cursor: Cursor = dao
        cursor.setNotificationUri(app.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}