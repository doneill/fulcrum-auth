package com.jdoneill.api

import com.jdoneill.common.ApplicationDispatcher
import com.jdoneill.model.FulcrumAuthenticationResponse

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readText

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class FulcrumApi {

    companion object {
        private const val BASE_URL = "https://api.fulcrumapp.com"
        private const val BASE_LOCAL_URL = "http://10.0.0.19:3000"

        // public endpoints
        private const val API_V2 = "/api/v2"
        private const val API_USERS = "$API_V2/users"
    }

    private val authClient = HttpClientAuthProvider().httpAuthClient

    fun getAccount(authorization: String, success: (FulcrumAuthenticationResponse) -> Unit, failure: (Throwable) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val response = authClient.get<HttpStatement> {
                    url("$BASE_LOCAL_URL$API_USERS")
                    header("Authorization", "Basic $authorization")
                }.execute()
                Json.nonstrict.parse(FulcrumAuthenticationResponse.serializer(), response.readText())
                    .also(success)
            } catch (e: Exception) {
                failure(e)
            }
        }
    }
}