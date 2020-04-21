package com.andrewedstrom.suspectdevicefilter

const val ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE = .009 // To ensure fpp is less than 1%, we shoot for .9%
const val DEFAULT_EXPECTED_INSERTIONS = 500000

/**
 * A fast and memory-efficient filter used to quickly identify potentially malicious devices that require further
 * processing downstream.
 *
 * It is the caller's responsibility to train the filter with known malicious devices, via [markDeviceAsSuspect].
 *
 * In order to keep its memory footprint small, implementations of this interface may report false positives at
 * a ratio of [ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE] for calls to [deviceIsSuspect].
 *
 */
interface SuspectDeviceFilter {
    /**
     * Marks a device as suspect.
     *
     * All future calls to [deviceIsSuspect] with the same [deviceId] will return true.
     */
    fun markDeviceAsSuspect(deviceId: String)

    /**
     * Reports whether [deviceId] is suspect and should be further vetted downstream.
     *
     * It is tolerable for implementations of this method to return false positives with a ratio of
     * [ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE]. False negatives, however, are unacceptable, as known malicious devices
     * must be flagged as such.
     */
    fun deviceIsSuspect(deviceId: String): Boolean
}

/**
 * This fake constructor is the preferred way of initializing a concrete instance of the [SuspectDeviceFilter] interface.
 *
 * @param expectedInsertions the maximum number of suspect devices that will be added to this filter. Adding more
 * devices than this will cause the false positive ratio to rise above [ACCEPTABLE_FALSE_POSITIVE_PERCENTAGE].
 */
@Suppress("FunctionName")
fun SuspectDeviceFilter(expectedInsertions: Int = DEFAULT_EXPECTED_INSERTIONS): SuspectDeviceFilter {
    return SuspectDeviceFilterImpl(expectedInsertions)
}