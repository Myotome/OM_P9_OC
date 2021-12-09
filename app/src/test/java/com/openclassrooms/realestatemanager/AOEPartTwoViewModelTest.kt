package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.parttwo.AOEPartTwoViewModel
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.parttwo.AOEPartTwoViewState
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AOEPartTwoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.getEstateById() } returns flow { generateOneEstateTest() }
    }

    @Test
    fun generateDetailViewState() {
        //Expect
        val viewState = getExpectedViewState()

        //Given
        val viewModelPartTwo = AOEPartTwoViewModel(dataSourceRepository)

        //Assert
        viewModelPartTwo.currentEstate!!.observeForever {
            Assert.assertEquals(viewState, it)
        }
    }

    private fun getExpectedViewState() = AOEPartTwoViewState(
        id = -5L,
        firestoreId = "123456",
        bedroom = 5,
        bathroom = 3,
        description = "estate to sell",
        realtor = "seller",
        dateEntry = 123456789
    )
}