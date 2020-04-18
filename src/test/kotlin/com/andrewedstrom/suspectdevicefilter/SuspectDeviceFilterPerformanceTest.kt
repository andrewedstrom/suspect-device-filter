package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SuspectDeviceFilterPerformanceTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilter

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilter()
    }

    @Test
    fun `It correctly flags all suspect devices with no false negatives`() {
        println("Training filter with 500000 suspect devices")
        val suspectDevices = (1..500000).map { getRandomDeviceId() }
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        println("Asserting that the filter correctly flags all suspect devices")
        suspectDevices.forEach {
            assertTrue(suspectDeviceFilter.mightBeSuspect(it))
        }
    }

    private fun getRandomDeviceId(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
        val stringLength = 5 //(62 allowedChars * 5) = 916132832, plenty for our purposes
        return (1..stringLength)
            .map { allowedChars.random() }
            .joinToString("")
    }
}