package com.jdoneill.common

// Mark the function dependent on platform implementation to be `expect`
expect fun getCurrentDate(): String

// This is the function which would be called by the Android or iOS app
fun getDate(): String {
    return getCurrentDate()
}
