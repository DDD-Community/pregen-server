package org.kkeunkkeun.pregen.account.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OauthTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("refresh_token")
    val refreshToken: String? = null,
    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int? = null,
)