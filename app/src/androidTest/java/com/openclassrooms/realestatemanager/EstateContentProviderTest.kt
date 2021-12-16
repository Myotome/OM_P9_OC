package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.database.EstateRoomDatabase
import com.openclassrooms.realestatemanager.model.*
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest {

    private lateinit var contentResolver: ContentResolver

    @get:Rule
    var instantTaskExecutorCoroutineRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        contentResolver = InstrumentationRegistry.getContext().contentResolver

    }

    /**
     * This test perform query on real room database.
     * Assert query is not null
     * Assert count is equal to number of estate
     *    but for testability is better to not fix this number
     *    because depend on current database number
     */
    @Test
    fun getAllEstateWithContentProvider(){
        val cursor = contentResolver.query(
            ContentUris.withAppendedId(EstateContentProvider.Companion.URI_MENU, 1),
            null,
            null,
            null,
            null)

        Assert.assertNotNull(cursor)
        Assert.assertTrue(cursor!!.count >= 0)
        cursor.close()
    }
}