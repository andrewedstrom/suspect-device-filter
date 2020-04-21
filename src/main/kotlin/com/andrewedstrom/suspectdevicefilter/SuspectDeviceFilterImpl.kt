package com.andrewedstrom.suspectdevicefilter

import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels
import java.io.Serializable
import java.nio.charset.Charset

/**
 * An implementation of [SuspectDeviceFilter] using a Bloom Filter as its backing store.
 *
 * It is very memory efficient. At full capacity it will be less than 15% the size of a naive hashmap-based implementation
 *
 * @param expectedInsertions the maximum number of suspect devices that will be added to this filter. Adding more
 * devices than this is allowed, but will cause the false positive ratio to rise above [ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE].
 */
@Suppress("UnstableApiUsage")
class SuspectDeviceFilterImpl(expectedInsertions: Int = DEFAULT_EXPECTED_INSERTIONS) :
    SuspectDeviceFilter, Serializable {

    private val bloomFilter = BloomFilter.create(
        Funnels.stringFunnel(Charset.defaultCharset()),
        expectedInsertions,
        ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE
    )

    override fun deviceIsSuspect(deviceId: String): Boolean {
        return bloomFilter.mightContain(deviceId)
    }

    override fun markDeviceAsSuspect(deviceId: String) {
        bloomFilter.put(deviceId)
    }

    companion object {
        private const val serialVersionUID = -91L
    }
}