package org.kkeunkkeun.pregen.account.domain.dto

class AccountLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val profileImg: String?,
)