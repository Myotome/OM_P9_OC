package com.openclassrooms.realestatemanager.utilsfortest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.model.*


fun generateOneEstateTest(): Estate {
    val listPhoto = ArrayList<Photo>()
    listPhoto.add(
        Photo(
//            image = "content://media/external/images/media/162",
            name = "view",
            storageId = "987654",
            storageUriString = ""
        )
    )
    listPhoto.add(
        Photo(
//            image = "content://media/external/images/media/158",
            name = "view",
            storageId = "854732",
            storageUriString = ""
        )
    )

    return Estate(
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
}

fun generateTwoEstateTest(): List<Estate> {
    val listPhoto = ArrayList<Photo>()
    listPhoto.add(
        Photo(
//            image = "content://media/external/images/media/162",
            name = "view",
            storageId = "987654",
            storageUriString = ""
        )
    )
    listPhoto.add(
        Photo(
//            image = "content://media/external/images/media/158",
            name = "view",
            storageId = "854732",
            storageUriString = ""
        )
    )

    val estateOne =  Estate(
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

    val estateTwo = Estate(-10L,
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
        ))

    return listOf(estateOne, estateTwo)
}

fun generateOneEstateWithoutLatLng(): List<Estate>{
    return listOf(Estate(
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
        lat = 0.0,
        lng = 0.0,
        listPhoto = listOf(Photo(
//            image = "content://media/external/images/media/162",
            name = "view",
            storageId = "987654",
            storageUriString = ""
        )),
        interest = Interest(
            school = true,
            store = false,
            park = true,
            restaurant = false,
            movie = true,
            theatre = false,
            subway = true,
            nightlife = false
        ))
    )
}

fun generateCurrentPositionTest() : LiveData<LatLng>{
    val currentPositionTest = MutableLiveData<LatLng>()
    val position = LatLng(40.712775,-74.0059717)
    currentPositionTest.value = position
    return currentPositionTest
}

fun generateQueryTest() : SimpleSQLiteQuery {
    val string = "SELECT * FROM estate_table WHERE estateType=?;"
    val args = ArrayList<Any>()
    args.add("Castle")
    return SimpleSQLiteQuery(string, args.toArray())

}