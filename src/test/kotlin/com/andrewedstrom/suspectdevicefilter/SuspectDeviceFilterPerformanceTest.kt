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
    fun `It correctly flags all known suspect devices with no false negatives`() {
        val suspectDevices = (1..500000).map { generateRandomString() }

        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }
        suspectDevices.forEach { assertTrue(suspectDeviceFilter.mightBeSuspect(it)) }
    }

    @Test
    fun `It has a false positive ratio less than 1%`() {
        val numberOfSuspectDevices = 500000
        println("Training filter with $numberOfSuspectDevices suspect devices")
        (1..numberOfSuspectDevices)
            .map { generateRandomString().toUpperCase() } // Suspect device ids are uppercase
            .forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        val numberOfInnocentDevices = 2000000
        val falsePositiveCount = (1..numberOfInnocentDevices)
            .map { generateRandomString().toLowerCase() } //Innocent devices ids are lowercase
            .count { suspectDeviceFilter.mightBeSuspect(it) }
        val falsePositivePercentage = falsePositiveCount / numberOfInnocentDevices.toDouble()

        println("Out of $numberOfInnocentDevices innocent devices, $falsePositiveCount were marked suspect")
        println("False positive ratio: $falsePositivePercentage")
        assertTrue(falsePositivePercentage < 0.01)
    }

    private fun generateRandomString(): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz1234567890"
        val stringLength = 6
        return (1..stringLength)
            .map { allowedChars.random() }
            .joinToString("")
    }
}