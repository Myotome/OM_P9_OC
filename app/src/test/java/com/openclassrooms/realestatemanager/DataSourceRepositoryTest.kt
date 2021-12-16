package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.openclassrooms.realestatemanager.database.EstateDAO
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.TestCoroutineRule
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import com.openclassrooms.realestatemanager.utilsfortest.generateQueryTest
import com.openclassrooms.realestatemanager.utilsfortest.generateTwoEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class DataSourceRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val estateDao = mockk<EstateDAO>()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    //Necessary to keep this for test even if it's not use
    private val firebaseApp = FirebaseApp.initializeApp(context);
    private val dataSourceRepositoryTest = DataSourceRepository(estateDao, context)

    @Before
    fun setUp() {
        every { estateDao.getAllEstate() } returns flowOf(generateTwoEstateTest())
        every { estateDao.getSearchEstate(generateQueryTest()) } returns flow { generateTwoEstateTest()[0] }
        every { estateDao.getCurrentEstate(-5L) } returns flow { generateOneEstateTest() }

    }

    @Test
    fun getAllEstateFromDAOTestWhenNoQuery() {
        //Expected
        val expected = generateTwoEstateTest()

        //Given
        dataSourceRepositoryTest.querySearchFlow.map { list ->

            //Assert
            Assert.assertTrue(list?.size == expected.size)
            Assert.assertTrue(list == expected)
        }

    }

    @Test
    fun getFilteredEstateFromDAOWhenQuery() {
        //Expected
        val expected = generateTwoEstateTest()[0]

        //Given
        dataSourceRepositoryTest.setSearchQuery(generateQueryTest())
        dataSourceRepositoryTest.querySearchFlow.map { result ->

            //Assert
            Assert.assertTrue(result?.size == 1)
            Assert.assertTrue((result?.get(0) == expected))
            Assert.assertTrue(result?.get(0)?.primaryEstateData?.estateType == "Castle")
            Assert.assertFalse(result?.get(0)?.primaryEstateData?.estateType == "Penthouse")
        }
    }

    @Test
    fun getEstateByIdTestReturnNull() {
        //Given
        dataSourceRepositoryTest.setCurrentEstateById(null)
        val result = dataSourceRepositoryTest.getEstateById()

        //Assert
        Assert.assertNull(result)
    }

    @Test
    fun getEstateByIdTest() {
        //Expected
        val expected = generateOneEstateTest()

        //Given
        dataSourceRepositoryTest.setCurrentEstateById(-5L)
        dataSourceRepositoryTest.getEstateById()?.map { estate ->

            //Assert
            Assert.assertTrue(estate == expected)
        }
    }
}