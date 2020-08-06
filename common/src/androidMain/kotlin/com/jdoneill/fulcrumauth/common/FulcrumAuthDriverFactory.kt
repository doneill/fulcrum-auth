package com.jdoneill.fulcrumauth.common

import android.content.Context
import com.jdoneill.fulcrumauth.db.FulcrumAuth
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class FulcrumAuthDriverFactory(private val appContext: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(FulcrumAuth.Schema, appContext, "FulcrumAuth.db")
    }
}