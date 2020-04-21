package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SuspectDeviceFilterImplTest {
    private lateinit var suspectDeviceFilter: SuspectDeviceFilterImpl

    @BeforeEach
    fun setup() {
        suspectDeviceFilter = SuspectDeviceFilterImpl()
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
}