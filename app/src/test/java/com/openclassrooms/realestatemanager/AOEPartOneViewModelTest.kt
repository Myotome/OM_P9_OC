package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.partone.AOEPartOneViewModel
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.partone.AOEPartOneViewState
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AOEPartOneViewModelTest {

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
        val viewModelPartOne = AOEPartOneViewModel(dataSourceRepository)

        //Assert
        viewModelPartOne.currentEstate!!.observeForever {
            Assert.assertEquals(viewState, it)
        }
    }

    private fun getExpectedViewState() = AOEPartOneViewState(
        onSale = true,
        type = "Penthouse",
        price = 8500501,
        surface = 99.0,
        room = 10,
        landSize = 555.0
    )

}