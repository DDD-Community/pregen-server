package org.kkeunkkeun.pregen.account.infrastructure.security.jwt.dto

data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)