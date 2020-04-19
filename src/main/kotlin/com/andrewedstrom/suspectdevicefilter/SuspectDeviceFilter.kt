package com.andrewedstrom.suspectdevicefilter

import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels
import java.nio.charset.Charset

const val DESIRED_FALSE_POSITIVE_PERCENTAGE = .009 // To ensure fpp less than 1%, we shoot for .9%
const val DEFAULT_EXPECTED_INSERTIONS = 500000

@Suppress("UnstableApiUsage")
class SuspectDeviceFilter(expectedInsertions: Int = DEFAULT_EXPECTED_INSERTIONS) {
    //TODO: validate input
    //TODO: handle failed filter creation
    val bloomFilter = BloomFilter.create(
        Funnels.stringFunnel(Charset.defaultCharset()),
        expectedInsertions,
        DESIRED_FALSE_POSITIVE_PERCENTAGE
    )

    fun mightBeSuspect(deviceId: String): Boolean {
        return bloomFilter.mightContain(deviceId)
    }

    fun markDeviceAsSuspect(deviceId: String) {
        bloomFilter.put(deviceId)
    }
}