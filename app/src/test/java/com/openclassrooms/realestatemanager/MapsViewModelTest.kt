package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.ui.main.maps.MapsEstateViewState
import com.openclassrooms.realestatemanager.ui.main.maps.MapsPositionViewState
import com.openclassrooms.realestatemanager.ui.main.maps.MapsViewModel
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.repository.LocationRepository
import com.openclassrooms.realestatemanager.repository.RetrofitRepository
import com.openclassrooms.realestatemanager.utilsfortest.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()
    private val locationRepository = mockk<LocationRepository>()
    private val retrofitRepository = mockk<RetrofitRepository>()

    @Before
    fun setUp() {

        every { locationRepository.getCurrentPosition() } returns generateCurrentPositionTest()
        every { dataSourceRepository.isSearching } returns MutableLiveData(false)


    }

    @FlowPreview
    @Test
    fun generateViewStateForMaps(){
        //Before
        every { dataSourceRepository.querySearchFlow } returns flowOf(generateTwoEstateTest())

        //Expect
        val viewStateExpect = getExpectedViewState()

        //Given
        val viewModelMaps = MapsViewModel(dataSourceRepository, locationRepository, retrofitRepository)
        viewModelMaps.getViewState().observeForTesting{

            //Assert
            Assert.assertEquals(viewStateExpect, it.value)
            Assert.assertTrue(it.value?.listEstate?.size == 2)
        }

    }

    private fun getExpectedViewState() = MapsPositionViewState(
        currentLat = 40.712775,
        currentLng = -74.0059717,
        listEstate=listOf(MapsEstateViewState(id=-5, lat=40.730489, lng=-73.99271), MapsEstateViewState(id=-10, lat=40.71164, lng=-73.997857))
    )

}