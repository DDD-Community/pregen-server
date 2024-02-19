package org.kkeunkkeun.pregen.account.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "oauth")
data class SocialClientProperties @ConstructorBinding constructor(
    val kakao: Social,
    val naver: Social,
) {

    fun kakaoRevokeUrl(): String {
        return "https://kapi.kakao.com/v1/user/unlink"
    }

    fun naverRevokeUrl(accessToken: String, provider: String): String {
        return "https://nid.naver.com/oauth2.0/token?"+
                "grant_type=delete" +
                "&client_id=${naver.clientId}" +
                "&client_secret=${naver.clientSecret}" +
                "&access_token=$accessToken" +
                "&service_provider=$provider"
    }

    fun googleRevokeUrl(accessToken: String): String {
        return "https://oauth2.googleapis.com/revoke?" +
                "token=$accessToken"
    }

    data class Social(
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String,
        val tokenUri: String,
        val userInfoUri: String,
        val scope: List<String>,
    )
}
