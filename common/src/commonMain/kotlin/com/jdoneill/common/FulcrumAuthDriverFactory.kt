package com.jdoneill.common

import com.jdoneill.db.FulcrumAuth
import com.squareup.sqldelight.db.SqlDriver

expect class FulcrumAuthDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDb(fulcrumAuthDriverFactory: FulcrumAuthDriverFactory): FulcrumAuth {
    val driver = fulcrumAuthDriverFactory.createDriver()

    return FulcrumAuth(driver)
}
