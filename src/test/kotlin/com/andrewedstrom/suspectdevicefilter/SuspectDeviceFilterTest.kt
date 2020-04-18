package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SuspectDeviceFilterTest {
    lateinit var suspectDeviceFilter: SuspectDeviceFilter

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilter()
    }

    @Test
    fun `mightBeSuspect returns false for unfamiliar devices`() {
        assertFalse(suspectDeviceFilter.mightBeSuspect("unknown-device"))
    }

    @Test
    fun `markDeviceAsSuspect marks devices as suspect`() {
        val suspectDeviceId = "known-vpn-server-id"
        suspectDeviceFilter.markDeviceAsSuspect(suspectDeviceId)
        assertTrue(suspectDeviceFilter.mightBeSuspect(suspectDeviceId))
    }
}