package com.jdoneill.fulcrumauth.api

import com.jdoneill.fulcrumauth.model.FulcrumAuthenticationResponse

interface FulcrumService {
    suspend fun getAccount(authorization: String): FulcrumAuthenticationResponse
}