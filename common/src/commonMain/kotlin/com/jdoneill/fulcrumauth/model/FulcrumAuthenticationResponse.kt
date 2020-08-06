package com.jdoneill.fulcrumauth.model

import kotlinx.serialization.Serializable

@Serializable
data class FulcrumAuthenticationResponse(
    val user: User
)

@Serializable
data class User(
    val first_name: String,
    val last_name: String,
    val email: String,
    val id: String,
    val contexts: List<Contexts>
)

@Serializable
data class Contexts(
    val name: String,
    val id: String,
    val api_token: String
)