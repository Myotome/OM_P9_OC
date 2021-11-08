package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
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

    private fun getContentProviderDao(context: Context): EstateDAO {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(context, EstateContentProviderDao::class.java)
        return hiltEntryPoint.contentProviderDao()
    }

    companion object {
        private const val AUTHORITY =
            "com.openclassrooms.realestatemanager.provider.EstateContentProvider"
        private const val TABLE_NAME = "estate_table"
        val URI_MENU: Uri = Uri.parse("content://$AUTHORITY/estate_table")
        private const val CODE_ESTATE_TABLE_DIR = 1
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_ESTATE_TABLE_DIR)
        }
    }


    override fun onCreate() = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return if (code == CODE_ESTATE_TABLE_DIR) {
            val app = context?.applicationContext ?: throw IllegalStateException()
            val daos: EstateDAO = getContentProviderDao(app)

            val cursor: Cursor =
                if (code == CODE_ESTATE_TABLE_DIR) daos.findAllEstate() else return null
            cursor.setNotificationUri(app.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
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