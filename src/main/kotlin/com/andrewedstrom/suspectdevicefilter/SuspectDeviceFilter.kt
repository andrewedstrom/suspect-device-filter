package com.andrewedstrom.suspectdevicefilter

interface SuspectDeviceFilter {
    fun deviceIsSuspect(deviceId: String): Boolean
    fun markDeviceAsSuspect(deviceId: String)
}