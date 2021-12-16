package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.main.detail.DetailViewModel
import com.openclassrooms.realestatemanager.ui.main.detail.DetailViewState
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.getEstateById() } returns flow { generateOneEstateTest() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun generateViewStateForDetail() {
        //Expect
        val viewState = getExpectedViewState()

        //Given
        val viewModelDetail = DetailViewModel(dataSourceRepository)

        //Assert
        viewModelDetail.detailLiveData.observeForever {
            Assert.assertEquals(viewState, it)
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAddressStringTest() {
        //Expect
        val addressExpected = "753   \nBroadway\nManhattan\nNew York"

        //Given
        val viewModelDetail = DetailViewModel(dataSourceRepository)
        viewModelDetail.detailLiveData.observeForever {
            val address = it.address

            //Assert
            Assert.assertEquals(addressExpected, address)
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getFormattedAddressTest() {
        //Expect
        val addressFormatExpect = "https://maps.googleapis.com/maps/api/" +
                "staticmap?size=200x200&scale=2" +
                "&center=753+Broadway+New+York" +
                "&markers=color:blue|40.730489,-73.99271" +
                "&key=${BuildConfig.MAPS_API_KEY}"

        //Given
        val viewModelDetail = DetailViewModel(dataSourceRepository)
        viewModelDetail.detailLiveData.observeForever {
            val address = it.formattedAddress

            //Assert
            Assert.assertEquals(addressFormatExpect, address)
        }
    }

    private fun getExpectedViewState(): DetailViewState =
        DetailViewState(
            id = -5L,
            type = "Penthouse",
            price = 8500501,
            surface = 99.0,
            rooms = 10,
            bedrooms = 5,
            bathrooms = 3,
            description = "estate to sell",
            landSize = 555.0,
            school = true,
            store = false,
            park = true,
            restaurant = false,
            movie = true,
            theatre = false,
            subway = true,
            nightlife = false,
            photoList = listOf(
                Photo(
                    name = "view",
                    storageId = "987654",
                    storageUriString = ""
                ), Photo(
                    name = "view",
                    storageId = "854732",
                    storageUriString = ""
                )
            ),
            address = "753   \nBroadway\nManhattan\nNew York",
            realtor = "seller",
            entryDate = Utils.getLongToString(123456789),
            modificationDate = "",
            soldDate = "",
            onSale = true,
            formattedAddress = "https://maps.googleapis.com/maps/api/" +
                    "staticmap?size=200x200&scale=2" +
                    "&center=753+Broadway+New+York" +
                    "&markers=color:blue|40.730489,-73.99271" +
                    "&key=${BuildConfig.MAPS_API_KEY}"
        )
}