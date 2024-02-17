package org.kkeunkkeun.pregen.account.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OauthTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,

    private val scope: String,
) {
}