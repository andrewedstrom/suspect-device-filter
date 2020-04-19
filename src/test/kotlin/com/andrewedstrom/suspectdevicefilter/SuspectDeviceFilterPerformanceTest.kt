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
        val suspectDevices = (1..500000).map { generateRandomString() }

        println("Training filter with 500000 suspect devices")
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        println("Asserting that the filter flags all suspect devices")
        suspectDevices.forEach { assertTrue(suspectDeviceFilter.mightBeSuspect(it)) }
    }

    @Test
    fun `It has a false positive ratio less than 1%`() {
        println("Training filter with 500000 suspect devices")
        (1..500000)
            .map { generateRandomString().toUpperCase() } // Suspect device ids are uppercase
            .forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        println("Testing 2 million innocent devices")
        val numberOfInnocentDevices = 2000000
        val falsePositiveCount = (1..numberOfInnocentDevices)
            .map { generateRandomString().toLowerCase() } //Innocent devices ids are lowercase
            .count { suspectDeviceFilter.mightBeSuspect(it) }

        val falsePositivePercentage = falsePositiveCount / numberOfInnocentDevices.toDouble()
        println("$falsePositiveCount innocent devices were marked suspect")
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