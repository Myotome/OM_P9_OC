package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun convertDollarToEuroTest(){
        val submit = 40
        val expect = 34.0904
        val result = Utils.convertDollarToEuro(submit)

        Assert.assertEquals(expect,result, 0.1)
    }

    @Test
    fun convertEuroToDollarTest(){
        val submit = 40
        val expect = 46.932
        val result = Utils.convertEuroToDollar(submit)

        Assert.assertEquals(expect, result, 0.1)
    }

    @Test
    fun getTodayDateTest(){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expect = dateFormat.format(Date())

        /**
         * variant to test
         * enter today date before test
         * in format "dd/MM/yyyy"
         */
//        val expect = "20/12/2021"

        val result = Utils.getTodayDate()

        Assert.assertEquals(expect, result)
    }

    @Test
    fun getLongFormatDateTest(){
        val expect = Calendar.getInstance().time.time.toFloat()
        val result = Utils.getLongFormatDate().toFloat()

        Assert.assertEquals(expect, result,2F)
    }

    @Test
    fun getLongFormatDateWithParametersTest(){
        val years = 2021
        val month = 12
        val day = 25
        val testCalendar = Calendar.getInstance()
        testCalendar.set(years,month,day)
        val expect = testCalendar.time.time.toFloat()

        val result = Utils.getLongFormatDate(years, month, day).toFloat()

        Assert.assertEquals(expect, result, 2F)
    }

    @Test
    fun getLongToStringTest(){
        val submit = 123456789L
        val expect = "02/01/1970"
        val result  = Utils.getLongToString(submit)

        Assert.assertEquals(expect,result)
    }
}