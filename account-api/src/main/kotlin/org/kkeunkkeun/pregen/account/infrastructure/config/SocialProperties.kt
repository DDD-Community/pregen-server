package org.kkeunkkeun.pregen.account.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.security.oauth2.client")
data class SocialClientProperties @ConstructorBinding constructor(
    val registration: Registrations,
) {

    fun kakaoRevokeUrl(): String {
        return "https://kapi.kakao.com/v1/user/unlink"
    }

    fun naverRevokeUrl(accessToken: String, provider: String): String {
        return "https://nid.naver.com/oauth2.0/token?"+
                "grant_type=delete" +
                "&client_id=${registration.naver.clientId}" +
                "&client_secret=${registration.naver.clientSecret}" +
                "&access_token=$accessToken" +
                "&service_provider=$provider"
    }

    fun googleRevokeUrl(accessToken: String): String {
        return "https://oauth2.googleapis.com/revoke?" +
                "token=$accessToken"
    }

    data class Registrations(
        val naver: Registration,
    ) {

        data class Registration(
            val clientId: String,
            val clientSecret: String,
        )
    }
}
