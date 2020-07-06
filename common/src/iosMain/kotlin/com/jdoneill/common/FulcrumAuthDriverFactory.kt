package com.jdoneill.common

import com.jdoneill.db.FulcrumAuth
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class FulcrumAuthDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(FulcrumAuth.Schema, "FulcrumAuth.db")
    }
}