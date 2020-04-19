package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

const val LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz"
const val UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

class SuspectDeviceFilterPerformanceTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilter

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilter()
    }

    @Test
    fun `It correctly flags all suspect devices with no false negatives`() {
        val suspectDevices = (1..500000).map { generateRandomDeviceIdFromCharacters() }

        println("Training filter with 500000 suspect devices")
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        println("Asserting that the filter flags all suspect devices")
        suspectDevices.forEach { assertTrue(suspectDeviceFilter.mightBeSuspect(it)) }
    }

    @Test
    fun `It has a false positive ratio less than 1%`() {
        println("Training filter with 500000 suspect devices")
        (1..500000)
            .map { generateRandomDeviceIdFromCharacters(UPPERCASE_CHARACTERS) }
            .forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        println("Testing 2 million innocent devices")
        val numberOfInnocentDevices = 2000000
        val falsePositiveCount = (1..numberOfInnocentDevices)
            .map { generateRandomDeviceIdFromCharacters(LOWERCASE_CHARACTERS) }
            .count { suspectDeviceFilter.mightBeSuspect(it) }

        val falsePositivePercentage = falsePositiveCount / numberOfInnocentDevices.toDouble()
        println("$falsePositiveCount innocent devices were marked suspect")
        println("False positive ratio: $falsePositivePercentage")
        assertTrue(falsePositivePercentage < 0.01)
    }

    private fun generateRandomDeviceIdFromCharacters(
        allowedChars: String = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS
    ): String {
        val stringLength = 6
        return (1..stringLength)
            .map { allowedChars.random() }
            .joinToString("")
    }
}