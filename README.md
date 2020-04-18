![](github.com/andrewedstrom/suspect-device-filter/workflows/unit-tests/badge.svg)

# Suspect Device Filter
![Good Will Hunting You're Suspect](https://i.imgur.com/6wjpgGi.jpg "Good Will Hunting You're Suspect")

A small kotlin library which can be used to flag requests from devices previously reported to be suspect.

This library is built around a `SuspectDeviceFilter` class, which is a fast and memory-efficient cache of known-suspect devices. Given a device ID, `suspectDeviceFilter.mightBeSuspect(deviceId: String)` will report whether a device is either
1) definitely not suspect, with absolute certainty
2) very likely suspect, with 99% accuracy

This is _not_ meant to be used as a definitive test of whether a device is suspect, as it will report false positives in 1% of cases. It serves as a in-memory cache which can quickly flag potentially suspicious requests to be further vetted downstream.

# Testing the code
```
./gradlew clean test
```
