package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.ui.main.list.ListViewModel
import com.openclassrooms.realestatemanager.ui.main.list.ListViewState
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.generateTwoEstateTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.querySearchFlow } returns flowOf(generateTwoEstateTest())
        every { dataSourceRepository.isSearching } returns MutableLiveData(false)
    }

    @Test
    fun generateListViewState()  {
        //Expect
        val expect = ListViewState(
            id = -5L,
            type = "Penthouse",
            district = "Manhattan",
            price = 8500501,
            onSale = true,
            photo = Photo(
//                image = "content://media/external/images/media/162",
                name = "view",
                storageId = "987654",
                storageUriString = ""
            )
        )

        //Given
        val viewModel = ListViewModel(dataSourceRepository)

        //when
        viewModel.uiStateLiveData.observeForever {
            Assert.assertEquals(expect, it[0])
            Assert.assertEquals(2, it.size)
        }

    }

}