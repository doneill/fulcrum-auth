package com.jdoneill.fulcrumauth.api

import com.jdoneill.fulcrumauth.model.FulcrumAuthenticationResponse

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readText

import kotlinx.serialization.json.Json

class FulcrumApi: FulcrumService {

    companion object {
        private const val BASE_URL = "https://api.fulcrumapp.com"

        // public endpoints
        private const val API_V2 = "/api/v2"
        private const val API_USERS = "$API_V2/users"
    }

    private val authClient = HttpClientAuthProvider().httpAuthClient

    override suspend fun getAccount(authorization: String): FulcrumAuthenticationResponse {
        val response = authClient.get<HttpStatement> {
            url("$BASE_URL$API_USERS")
            header("Authorization", "Basic $authorization")
        }.execute()

        val json = Json {
            ignoreUnknownKeys = true
        }

        return json.decodeFromString(FulcrumAuthenticationResponse.serializer(), response.readText())
    }
}