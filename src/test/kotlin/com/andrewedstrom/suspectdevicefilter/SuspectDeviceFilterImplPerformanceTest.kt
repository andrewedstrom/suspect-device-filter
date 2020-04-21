package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class SuspectDeviceFilterImplPerformanceTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilterImpl
    private val expectedInsertions = 600000

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilterImpl(expectedInsertions)
    }

    @Test
    fun `It correctly flags all known suspect devices with zero false negatives`() {
        val suspectDevices = generateRandomDevices(expectedInsertions)
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }
        suspectDevices.forEach { assertTrue(suspectDeviceFilter.deviceIsSuspect(it)) }
    }

    @Test
    fun `It reports false positives for fewer than 1% of innocent devices`() {
        val suspectDevices = generateRandomDevices(expectedInsertions).map { it.toUpperCase() }
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }

        val numInnocentDevices = 2000000
        val innocentDevices = generateRandomDevices(numInnocentDevices).map { it.toLowerCase() }
        val falsePositives = innocentDevices.count { suspectDeviceFilter.deviceIsSuspect(it) }

        val falsePositivePercent = (falsePositives / numInnocentDevices.toDouble()) * 100

        println("Worst-case false positive percent is %.2f%%".format(falsePositivePercent))
        assertTrue(falsePositivePercent < 1.0)
    }

    @Test
    fun `It is much more memory efficient than a naive hashmap-based implementation`() {
        val naiveSuspectDeviceFilter = NaiveSuspectDeviceFilter()

        val suspectDevices = generateRandomDevices(expectedInsertions)
        suspectDevices.forEach { suspectDeviceFilter.markDeviceAsSuspect(it) }
        suspectDevices.forEach { naiveSuspectDeviceFilter.markDeviceAsSuspect(it) }

        val naiveImplementationSize = sizeOfSerializedObjectInBytes(naiveSuspectDeviceFilter)
        val trueImplementationSize = sizeOfSerializedObjectInBytes(suspectDeviceFilter)
        println("Size of naive implementation after training: $naiveImplementationSize bytes")
        println("Size of real implementation after training: $trueImplementationSize bytes")
        assertTrue(trueImplementationSize < naiveImplementationSize)

        val sizeComparisonRatio = (trueImplementationSize / naiveImplementationSize.toDouble()) * 100
        println(
            "At max capacity, real implementation is %.2f%% the size of the naive implementation".format(
                sizeComparisonRatio
            )
        )
        assertTrue(sizeComparisonRatio < 15.0)
    }

    private fun generateRandomDevices(numToGenerate: Int) = (1..numToGenerate).map { generateRandomDeviceId() }

    private fun generateRandomDeviceId(): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz1234567890"
        val stringLength = 6
        return (1..stringLength).map { allowedChars.random() }.joinToString("")
    }

    private fun sizeOfSerializedObjectInBytes(obj: Serializable): Int {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        objectOutputStream.close()
        return byteArrayOutputStream.toByteArray().size
    }
}

class NaiveSuspectDeviceFilter : SuspectDeviceFilter, Serializable {
    private val knownSuspectDevices = HashSet<String>()

    override fun deviceIsSuspect(deviceId: String): Boolean {
        return knownSuspectDevices.contains(deviceId)
    }

    override fun markDeviceAsSuspect(deviceId: String) {
        knownSuspectDevices.add(deviceId)
    }

    companion object {
        private const val serialVersionUID = -4244985L
    }
}