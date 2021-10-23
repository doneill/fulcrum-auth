package com.jdoneill.fulcrumauth.db

import com.google.common.truth.Truth.assertThat
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

import org.junit.Before
import org.junit.Test

class AuthDatabaseTest {

    private lateinit var userId: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String

    private lateinit var queries: FulcrumAuthModelQueries

    @Before
    fun setup() {
        userId = "0d6f24b2-f10f-43b6-b5b4-eb3f96bda562"
        firstName = "Test"
        lastName = "Testing"
        email = "test@testing.com"

        val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            FulcrumAuth.Schema.create(this)
        }
        queries = FulcrumAuth(inMemorySqlDriver).fulcrumAuthModelQueries
    }

    @Test
    fun emptyUsersAndOrgsTest() {
        val emptyUsers = queries.selectAllUsers().executeAsList()
        assertThat(emptyUsers).isEmpty()

        val emptyOrgs = queries.selectAllOrganizations().executeAsList()
        assertThat(emptyOrgs).isEmpty()
    }

    @Test
    fun insertUsersTest() {
        queries.insertUsers(userId, firstName, lastName, email)

        val users = queries.selectAllUsers().executeAsList()
        assertThat(users).hasSize(1)
    }
}