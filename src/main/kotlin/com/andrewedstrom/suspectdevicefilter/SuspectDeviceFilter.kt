package com.andrewedstrom.suspectdevicefilter

interface SuspectDeviceFilter {
    fun deviceIsSuspect(deviceId: String): Boolean
    fun markDeviceAsSuspect(deviceId: String)
}

// Fake "constructor" factory method that returns default implementation
@Suppress("FunctionName")
fun SuspectDeviceFilter(expectedInsertions: Int = DEFAULT_EXPECTED_INSERTIONS): SuspectDeviceFilter {
    return SuspectDeviceFilterImpl(expectedInsertions)
}