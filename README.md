![](https://github.com/andrewedstrom/suspect-device-filter/workflows/unit-tests/badge.svg)

# Suspect Device Filter
![Good Will Hunting Ya Suspect](https://i.imgur.com/6wjpgGi.jpg "Good Will Hunting Ya Suspect")

A small kotlin library which can be used to determine with high accuracy whether a device has been previously marked as suspect. 

This library is built around a `SuspectDeviceFilter` class, which is a fast and memory-efficient cache of known-suspect devices.

It is the caller's responsibility to train the filter. The filter only knows what devices to flag based on previous calls to `suspectDeviceFilter.markDeviceAsSuspect(deviceId: String)`. 

Given a device ID, `suspectDeviceFilter.mightBeSuspect(deviceId: String)` will report whether a device is either:
1) definitely not known to be suspect
2) very likely known to be suspect

This library is _not_ meant to be used as a definitive test of whether a device is suspect, as it will report false positives in as many as 1% of cases. Instead, it serves as an in-memory cache which can quickly flag potentially suspicious requests to be further vetted downstream.

# Testing the code
The following will run both functionality and performance tests.
```
./gradlew clean test
```

Results of the performance tests will be printed to standard out, and the test suite will fail if performance falls below acceptable values. 
