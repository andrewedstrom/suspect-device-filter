package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SuspectDeviceFilterImplPerformanceTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilterImpl
    private val expectedInsertions = 600000

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilterImpl(expectedInsertions)
    }

    @Test
    fun `It correctly flags all known suspect devices with zero false negatives`() {
        val suspectDevices = (1..expectedInsertions).map { generateRandomString() }

        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }
        suspectDevices.forEach { assertTrue(suspectDeviceFilter.mightBeSuspect(it)) }
    }

    @Test
    fun `It reports false positives for fewer than 1% of innocent devices`() {
        // We chose to train the filter with the maximum number of expected insertions because
        // that will give us the worst-case false positive percentage.
        //
        // A smaller number of suspect devices will always yield a lower percentage of false positives
        println("Training filter with $expectedInsertions suspect devices")
        (1..expectedInsertions)
            .map { generateRandomString().toUpperCase() } // Suspect device ids are uppercase
            .forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        val numberOfInnocentDevices = 2000000
        println("Testing $numberOfInnocentDevices innocent devices")
        val falsePositiveCount = (1..numberOfInnocentDevices)
            .map { generateRandomString().toLowerCase() } //Innocent devices ids are lowercase
            .count { suspectDeviceFilter.mightBeSuspect(it) }
        val falsePositivePercentage = falsePositiveCount / numberOfInnocentDevices.toDouble()

        println("$falsePositiveCount innocent devices were incorrectly marked suspect")
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