package com.andrewedstrom.suspectdevicefilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SuspectDeviceFilterTest {
    @Test
    fun `isSuss returns true`() {
        assertTrue(SuspectDeviceFilter().isSuss())
    }
}
