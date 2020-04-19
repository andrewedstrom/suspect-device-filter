package com.andrewedstrom.suspectdevicefilter

interface SuspectDeviceFilter {
    fun mightBeSuspect(deviceId: String): Boolean
    fun markDeviceAsSuspect(deviceId: String)
}