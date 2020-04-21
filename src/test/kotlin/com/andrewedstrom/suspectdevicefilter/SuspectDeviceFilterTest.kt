package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SuspectDeviceFilterTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilter

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilter()
    }

    @Test
    fun `deviceIsSuspect returns false for unfamiliar devices`() {
        assertFalse(suspectDeviceFilter.deviceIsSuspect("unknown-device"))
    }

    @Test
    fun `markDeviceAsSuspect marks devices as suspect`() {
        val suspectDeviceId = "known-vpn-server-id"
        suspectDeviceFilter.markDeviceAsSuspect(suspectDeviceId)
        assertTrue(suspectDeviceFilter.deviceIsSuspect(suspectDeviceId))
        assertFalse(suspectDeviceFilter.deviceIsSuspect("innocent-device-id"))
    }

    @Test
    fun `The fake constructor returns a instance of SuspectDeviceFilterImpl with the default expectedInsertions`() {
        val defaultSuspectDeviceFilter = SuspectDeviceFilter()
        assertTrue(defaultSuspectDeviceFilter is SuspectDeviceFilterImpl)
        assertEquals((defaultSuspectDeviceFilter as SuspectDeviceFilterImpl).expectedInsertions, 500000)
    }

    @Test
    fun `The fake constructor correctly sets the expectedInsertions when they are provided`() {
        val expectedInsertions = 1993
        val suspectDeviceFilter = SuspectDeviceFilter(expectedInsertions)
        assertEquals((suspectDeviceFilter as SuspectDeviceFilterImpl).expectedInsertions, expectedInsertions)
    }
}