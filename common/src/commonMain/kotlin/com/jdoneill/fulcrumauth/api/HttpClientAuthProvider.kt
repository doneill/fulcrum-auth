package com.jdoneill.fulcrumauth.api

import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

class HttpClientAuthProvider {
    val httpAuthClient = HttpClient {
        install(Auth) {
            basic {
                username = "user"
                password = "password"
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

    }
}