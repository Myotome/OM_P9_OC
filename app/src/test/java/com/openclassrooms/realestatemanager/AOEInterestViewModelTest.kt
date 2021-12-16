package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.interest.AOEInterestViewModel
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.interest.AOEInterestViewState
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AOEInterestViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.getEstateById() } returns flow { generateOneEstateTest() }
    }

    @DelicateCoroutinesApi
    @Test
    fun generateDetailViewStateForInterest() {
        //Expect
        val viewState = getExpectedViewState()

        //Given
        val viewModelInterest = AOEInterestViewModel(dataSourceRepository)

        //Assert
        viewModelInterest.currentEstate!!.observeForever {
            Assert.assertEquals(viewState, it)
        }
    }

    private fun getExpectedViewState() = AOEInterestViewState(
        school = true,
        store = false,
        park = true,
        restaurant = false,
        movie = true,
        theatre = false,
        subway = true,
        nightlife = false
    )
}