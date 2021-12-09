package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.address.AOEAddressViewModel
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.address.AOEAddressViewState
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.repository.RetrofitRepository
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AOEAddressViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()
    private val retrofitRepository = mockk<RetrofitRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.getEstateById() } returns flow { generateOneEstateTest() }
    }

    @Test
    fun generateDetailViewState() {
        //Expect
        val viewState = getExpectedViewState()

        //Given
        val viewModelAddress = AOEAddressViewModel(dataSourceRepository, retrofitRepository)

        //Assert
        viewModelAddress.currentEstate!!.observeForever {
            Assert.assertEquals(viewState, it)
        }
    }

    private fun getExpectedViewState() = AOEAddressViewState(
        number = 753,
        complement = null,
        street = "Broadway",
        district = "Manhattan",
        city = "New York",
        lat = 40.730489,
        lng = -73.99271
    )
}