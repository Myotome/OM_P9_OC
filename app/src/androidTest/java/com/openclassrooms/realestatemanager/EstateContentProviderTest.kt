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

//    private lateinit var databaseTest: EstateRoomDatabase
//    private lateinit var estateDaoTest: EstateDAO
    private lateinit var contentResolver: ContentResolver

    @get:Rule
    var instantTaskExecutorCoroutineRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
//        databaseTest = Room.inMemoryDatabaseBuilder(
//            InstrumentationRegistry.getContext(),
//            EstateRoomDatabase::class.java
//        ).allowMainThreadQueries()
//            .build()
//        estateDaoTest = databaseTest.EstateDAO()
        contentResolver = InstrumentationRegistry.getContext().contentResolver

    }
//
//    @After
//    fun closeDb() {
//        databaseTest.close()
//    }

    @Test
    fun getEstateWhenNoEstateInserted(){
        val cursor = contentResolver.query(
            ContentUris.withAppendedId(EstateContentProvider.Companion.URI_MENU, 1),
            null,
            null,
            null,
            null)

        Assert.assertNotNull(cursor)
        Assert.assertTrue(cursor!!.count == 4)
        cursor.close()
    }

//    @Test
//    fun getEstateWhenTwoEstateInserted() = runBlocking {
//        getTwoEstate().forEach { estate -> estateDaoTest.insertEstate(estate) }
//
//        val cursor = contentResolver.query(
//            ContentUris.withAppendedId(EstateContentProvider.Companion.URI_MENU, 1),
//            null,
//            null,
//            null,
//            null)
//
//        Assert.assertNotNull(cursor)
//        Assert.assertTrue(cursor?.count == 2)
////        cursor?.close()
//
//
//    }
//
//    private fun getTwoEstate(): List<Estate> {
//        val listPhoto = ArrayList<Photo>()
//        listPhoto.add(
//            Photo(
//                image = "content://media/external/images/media/162",
//                name = "view",
//                storageId = "987654",
//                storageUriString = ""
//            )
//        )
//        listPhoto.add(
//            Photo(
//                image = "content://media/external/images/media/158",
//                name = "view",
//                storageId = "854732",
//                storageUriString = ""
//            )
//        )
//
//        val estateOne = Estate(
//            -5L,
//            primaryEstateData = PrimaryEstateData(
//                onSale = true,
//                estateType = "Penthouse",
//                price = 8500501,
//                surface = 99.0,
//                rooms = 10,
//                landSize = 555.0,
//                null
//            ),
//            secondaryEstateData = SecondaryEstateData(
//                firebaseId = "123456",
//                bedroom = 5,
//                bathroom = 3,
//                description = "estate to sell",
//                realtor = "seller",
//                entryDate = 123456789,
//                modificationDate = null
//            ),
//            address = Address(
//                district = "Manhattan",
//                number = 753,
//                complement = null,
//                street = "Broadway",
//                city = "New York"
//            ),
//            lat = 40.730489,
//            lng = -73.99271,
//            listPhoto = listPhoto,
//            interest = Interest(
//                school = true,
//                store = false,
//                park = true,
//                restaurant = false,
//                movie = true,
//                theatre = false,
//                subway = true,
//                nightlife = false
//            )
//        )
//
//        val estateTwo = Estate(
//            -10L,
//            primaryEstateData = PrimaryEstateData(
//                onSale = false,
//                estateType = "Castle",
//                price = 100000000,
//                surface = 500.0,
//                rooms = 50,
//                landSize = 15000.0,
//                soldDate = 123456789
//            ),
//            secondaryEstateData = SecondaryEstateData(
//                firebaseId = "654321",
//                bedroom = 20,
//                bathroom = 15,
//                description = "Castle sold",
//                realtor = "seller",
//                entryDate = 123456789,
//                modificationDate = null
//            ),
//            address = Address(
//                district = "Manhattan",
//                number = 50,
//                complement = null,
//                street = "Madison street",
//                city = "New York"
//            ),
//            lat = 40.71164,
//            lng = -73.997857,
//            listPhoto = listPhoto,
//            interest = Interest(
//                school = false,
//                store = false,
//                park = false,
//                restaurant = true,
//                movie = true,
//                theatre = true,
//                subway = true,
//                nightlife = false
//            )
//        )
//
//        return listOf(estateOne, estateTwo)
//    }


}