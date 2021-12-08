package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utilsforinstrutest.Utils
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun testConvertDollarToEuro(){
        val submit = 40
        val expect = 34.0904
        val result = Utils.convertDollarToEuro(submit)

        Assert.assertEquals(expect,result, 0.1)
    }

    @Test
    fun testConvertEuroToDollar(){
        val submit = 40
        val expect = 46.932
        val result = Utils.convertEuroToDollar(submit)

        Assert.assertEquals(expect, result, 0.1)
    }

    @Test
    fun testGetTodayDate(){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expect = dateFormat.format(Date())

        /**
         * variant to test
         * enter today date before test
         */
//        val expect = "02/12/2021"

        val result = Utils.getTodayDate()

        Assert.assertEquals(expect, result)
    }

    @Test
    fun testGetLongFormatDate(){
        val expect = Calendar.getInstance().time.time.toFloat()
        val result = Utils.getLongFormatDate().toFloat()

        Assert.assertEquals(expect, result,2F)
    }

    @Test
    fun testGetLongFormatDateWithParameters(){
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
    fun testGetLongToString(){
        val submit = 123456789L
        val expect = "02/01/1970"
        val result  = Utils.getLongToString(submit)

        Assert.assertEquals(expect,result)
    }
}