package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.photos.AOEPhotoViewModel
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.photos.AOEPhotoViewState
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.repository.DataSourceRepository
import com.openclassrooms.realestatemanager.utilsfortest.TestCoroutineRule
import com.openclassrooms.realestatemanager.utilsfortest.generateOneEstateTest
import com.openclassrooms.realestatemanager.utilsfortest.getOrAwaitValue
import com.openclassrooms.realestatemanager.utilsfortest.observeForTesting
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AOEPhotoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dataSourceRepository = mockk<DataSourceRepository>()

    @Before
    fun setUp() {
        every { dataSourceRepository.getEstateById() } returns flow { emit(generateOneEstateTest()) }

    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun getViewStateTest() = testCoroutineRule.runBlockingTest  {
        //Expect
        val viewStateExpect = getExpectedViewState()

        //Given
        val viewModelPhoto = AOEPhotoViewModel(dataSourceRepository)

        //useful for waiting first data in mediator combine methode
        viewModelPhoto.listPhotoLive().observeForTesting {  }

        //True assertion with data from repository
        viewModelPhoto.listPhotoLive().observeForTesting {
            //Assert
            Assert.assertEquals(viewStateExpect, it.getOrAwaitValue())
        }

    }

    private fun getExpectedViewState() = AOEPhotoViewState(
        id = -5L,
        listPhoto = listOf(
            Photo(
//                image = "content://media/external/images/media/162",
                name = "view",
                storageId = "987654",
                storageUriString = ""
            ), Photo(
//                image = "content://media/external/images/media/158",
                name = "view",
                storageId = "854732",
                storageUriString = ""
            )
        )
    )

}