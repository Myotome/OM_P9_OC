package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.database.EstateRoomDatabase
import com.openclassrooms.realestatemanager.model.*
import com.openclassrooms.realestatemanager.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstateDAOTest {

    private lateinit var databaseTest: EstateRoomDatabase
    private lateinit var estateDaoTest: EstateDAO

    @get:Rule
    var instantTaskExecutorCoroutineRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        databaseTest = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EstateRoomDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        estateDaoTest = databaseTest.EstateDAO()
    }

    @After
    fun closeDb() {
        databaseTest.close()
    }

    @Test
    fun getItemWhenNoIteInserted() = runBlocking {
        val listEstate = estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertEquals(0, listEstate!!.size)
        Assert.assertNotNull(listEstate)
    }


    @Test
    fun insertInDatabaseTest() = runBlocking {
        val estate = getOneEstate()
        estateDaoTest.insertEstate(estate)

        val allEstate = estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()

        Assert.assertTrue(allEstate!!.contains(estate))
    }

    @Test
    fun getAllEstateTest() = runBlocking {
        getTwoEstate().forEach { estate -> estateDaoTest.insertEstate(estate) }

        val getAllEstate =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertEquals(2, getAllEstate!!.size)
    }

    @Test
    fun updateInDatabaseTest() = runBlocking {
        val estate = getOneEstate()
        estateDaoTest.insertEstate(estate)
        val getAllEstateBefore =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertTrue(getAllEstateBefore!![0].id == -5L)
        Assert.assertTrue(getAllEstateBefore[0].lat == 40.730489)

        val estateModified = estate.copy(lat = 20.50)
        estateDaoTest.updateEstate(estateModified)

        val getAllEstateUpdate =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertTrue(getAllEstateUpdate!![0].lat == 20.50)
        Assert.assertTrue(getAllEstateUpdate[0].id == -5L)
    }

    @Test
    fun updateLatLngByIdTest() = runBlocking {
        getTwoEstate().forEach { estate -> estateDaoTest.insertEstate(estate) }

        val getAllEstateBefore =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertEquals(2, getAllEstateBefore!!.size)
        Assert.assertTrue(getAllEstateBefore[0].id == -10L)
        Assert.assertTrue(getAllEstateBefore[0].lat == 40.71164)
        Assert.assertTrue(getAllEstateBefore[1].id == -5L)
        Assert.assertTrue(getAllEstateBefore[1].lat == 40.730489)

        estateDaoTest.updateLatLngById(-5L, 0.0, 0.0, 123456789)

        val getAllEstateUpdate =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertEquals(2, getAllEstateUpdate!!.size)
        Assert.assertTrue(getAllEstateUpdate[0].id == -10L)
        Assert.assertTrue(getAllEstateUpdate[0].lat == 40.71164)
        Assert.assertTrue(getAllEstateUpdate[1].id == -5L)
        Assert.assertTrue(getAllEstateUpdate[1].lat == 0.0)
    }

    @Test
    fun getCurrentEstateTest() = runBlocking {
        getTwoEstate().forEach { estate -> estateDaoTest.insertEstate(estate) }
        val getAllEstateBefore =
            estateDaoTest.getAllEstate().asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertEquals(2, getAllEstateBefore!!.size)
        Assert.assertTrue(getAllEstateBefore[0].id == -10L)
        Assert.assertTrue(getAllEstateBefore[1].id == -5L)

        val getOneEstate =
            estateDaoTest.getCurrentEstate(-5L).asLiveData(Dispatchers.Main).getOrAwaitValue()
        Assert.assertTrue(getOneEstate.id == -5L)
        Assert.assertFalse(getOneEstate.id == -10L)
    }

    @Test
    fun getSearchEstateTest() = runBlocking {
        getTwoEstate().forEach { estate -> estateDaoTest.insertEstate(estate) }

        val args = ArrayList<Any>()
        val stringQuery = "SELECT * FROM estate_table WHERE estateType=?;"
        args.add("Castle")

        val query = SimpleSQLiteQuery(stringQuery, args.toArray())
        val getEstateQuerySearch =
            estateDaoTest.getSearchEstate(query).asLiveData(Dispatchers.Main).getOrAwaitValue()

        Assert.assertTrue(getEstateQuerySearch!!.size == 1)
        Assert.assertTrue(getEstateQuerySearch[0].primaryEstateData.estateType == "Castle")
    }

    private fun getOneEstate() = Estate(
        -5L,
        primaryEstateData = PrimaryEstateData(
            onSale = true,
            estateType = "Penthouse",
            price = 8500501,
            surface = 99.0,
            rooms = 10,
            landSize = 555.0,
            null
        ),
        secondaryEstateData = SecondaryEstateData(
            firebaseId = "123456",
            bedroom = 5,
            bathroom = 3,
            description = "estate to sell",
            realtor = "seller",
            entryDate = 123456789,
            modificationDate = null
        ),
        address = Address(
            district = "Manhattan",
            number = 753,
            complement = null,
            street = "Broadway",
            city = "New York"
        ),
        lat = 40.730489,
        lng = -73.99271,
        listPhoto = listOf(
            Photo(
                name = "view",
                storageId = "987654",
                storageUriString = ""
            )
        ),
        interest = Interest(
            school = true,
            store = false,
            park = true,
            restaurant = false,
            movie = true,
            theatre = false,
            subway = true,
            nightlife = false
        )
    )

    private fun getTwoEstate(): List<Estate> {
        val listPhoto = ArrayList<Photo>()
        listPhoto.add(
            Photo(
                name = "view",
                storageId = "987654",
                storageUriString = ""
            )
        )
        listPhoto.add(
            Photo(
                name = "view",
                storageId = "854732",
                storageUriString = ""
            )
        )

        val estateOne = Estate(
            -5L,
            primaryEstateData = PrimaryEstateData(
                onSale = true,
                estateType = "Penthouse",
                price = 8500501,
                surface = 99.0,
                rooms = 10,
                landSize = 555.0,
                null
            ),
            secondaryEstateData = SecondaryEstateData(
                firebaseId = "123456",
                bedroom = 5,
                bathroom = 3,
                description = "estate to sell",
                realtor = "seller",
                entryDate = 123456789,
                modificationDate = null
            ),
            address = Address(
                district = "Manhattan",
                number = 753,
                complement = null,
                street = "Broadway",
                city = "New York"
            ),
            lat = 40.730489,
            lng = -73.99271,
            listPhoto = listPhoto,
            interest = Interest(
                school = true,
                store = false,
                park = true,
                restaurant = false,
                movie = true,
                theatre = false,
                subway = true,
                nightlife = false
            )
        )

        val estateTwo = Estate(
            -10L,
            primaryEstateData = PrimaryEstateData(
                onSale = false,
                estateType = "Castle",
                price = 100000000,
                surface = 500.0,
                rooms = 50,
                landSize = 15000.0,
                soldDate = 123456789
            ),
            secondaryEstateData = SecondaryEstateData(
                firebaseId = "654321",
                bedroom = 20,
                bathroom = 15,
                description = "Castle sold",
                realtor = "seller",
                entryDate = 123456789,
                modificationDate = null
            ),
            address = Address(
                district = "Manhattan",
                number = 50,
                complement = null,
                street = "Madison street",
                city = "New York"
            ),
            lat = 40.71164,
            lng = -73.997857,
            listPhoto = listPhoto,
            interest = Interest(
                school = false,
                store = false,
                park = false,
                restaurant = true,
                movie = true,
                theatre = true,
                subway = true,
                nightlife = false
            )
        )

        return listOf(estateOne, estateTwo)
    }

}