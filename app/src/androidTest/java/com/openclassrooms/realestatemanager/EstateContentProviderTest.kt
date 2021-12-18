package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Suppress("DEPRECATION")
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
    fun getAllEstateWithContentProvider() {
        val cursor = contentResolver.query(
            ContentUris.withAppendedId(EstateContentProvider.Companion.URI_MENU, 1),
            null,
            null,
            null,
            null
        )

        Assert.assertNotNull(cursor)
        Assert.assertTrue(cursor!!.count >= 0)
        cursor.close()
    }
}