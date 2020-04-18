package com.andrewedstrom.suspectdevicefilter

import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels
import java.nio.charset.Charset

const val ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE = .01
const val DEFAULT_EXPECTED_INSERTIONS = 500000

@Suppress("UnstableApiUsage") //TODO: Justify usage of unstable API
class SuspectDeviceFilter(expectedInsertions: Int = DEFAULT_EXPECTED_INSERTIONS) {
    //TODO: validate input
    //TODO: handle failed filter creation
    val bloomFilter = BloomFilter.create(
        Funnels.stringFunnel(Charset.defaultCharset()),
        expectedInsertions,
        ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE
    )

    fun mightBeSuspect(deviceId: String): Boolean {
        return bloomFilter.mightContain(deviceId)
    }

    fun markDeviceAsSuspect(deviceId: String) {
        bloomFilter.put(deviceId)
    }
}