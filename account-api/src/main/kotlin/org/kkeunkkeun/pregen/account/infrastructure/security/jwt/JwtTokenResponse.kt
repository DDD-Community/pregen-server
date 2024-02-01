package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
) {
}