package com.andrewedstrom.suspectdevicefilter

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator
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
        println("Training filter with $expectedInsertions suspect devices, to demonstrate worst-case false positive percentage")
        (1..expectedInsertions)
            .map { generateRandomString().toUpperCase() } // Suspect device ids are uppercase
            .forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        val numInnocentDevices = 2000000
        println("Testing $numInnocentDevices innocent devices")
        val falsePositiveCount = (1..numInnocentDevices)
            .map { generateRandomString().toLowerCase() } //Innocent devices ids are lowercase
            .count { suspectDeviceFilter.mightBeSuspect(it) }
        println("$falsePositiveCount innocent devices were incorrectly marked suspect")

        val falsePositivePercent = (falsePositiveCount / numInnocentDevices.toDouble()) * 100
        println("False positive percent: %.2f%%".format(falsePositivePercent))
        assertTrue(falsePositivePercent < 1)
    }

    @Test
    fun `It takes up much less space in memory than a naive hashmap-based implementation`() {
        // Hashmap-based implementation to which we will compare SuspectDeviceFilterImpl
        val naiveSuspectDeviceFilter = NaiveSuspectDeviceFilter()

        val suspectDevices = (1..expectedInsertions).map { generateRandomString() }
        println("Training both SuspectDeviceFilterImpl and naive hash map implementation with $expectedInsertions suspect devices")
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }
        suspectDevices.forEach { naiveSuspectDeviceFilter.markDeviceAsSuspect(it) }

        val naiveImplementationSize = ObjectSizeCalculator.getObjectSize(naiveSuspectDeviceFilter)
        val trueImplementationSize = ObjectSizeCalculator.getObjectSize(suspectDeviceFilter)

        println("Size of naive implementation after training: $naiveImplementationSize")
        println("Size of real implementation after training: $trueImplementationSize")

        val sizeComparisonRatio = (trueImplementationSize / naiveImplementationSize.toDouble()) * 100
        println("Real implementation is %.2f%% the size of the naive implementation".format(sizeComparisonRatio))
        assertTrue(naiveImplementationSize > trueImplementationSize)
    }

    inner class NaiveSuspectDeviceFilter : SuspectDeviceFilter {
        private val knownSuspectDevices = HashSet<String>()

        override fun mightBeSuspect(deviceId: String): Boolean {
            return knownSuspectDevices.contains(deviceId)
        }

        override fun markDeviceAsSuspect(deviceId: String) {
            knownSuspectDevices.add(deviceId)
        }
    }

    private fun generateRandomString(): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz1234567890"
        val stringLength = 6
        return (1..stringLength).map { allowedChars.random() }.joinToString("")
    }
}