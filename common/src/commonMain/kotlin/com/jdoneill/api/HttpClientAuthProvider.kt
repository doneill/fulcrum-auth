package com.jdoneill.api

import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic

class HttpClientAuthProvider {
    val httpAuthClient = HttpClient {
        install(Auth) {
            basic {
                username = "user"
                password = "password"
            }
        }
    }
}