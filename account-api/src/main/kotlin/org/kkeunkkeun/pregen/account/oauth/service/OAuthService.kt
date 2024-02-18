package org.kkeunkkeun.pregen.account.oauth.service

import org.kkeunkkeun.pregen.account.oauth.domain.SocialAuthToken
import org.kkeunkkeun.pregen.account.domain.SocialProvider
import org.kkeunkkeun.pregen.account.domain.dto.OauthTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.config.SocialClientProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Service
class OAuthService(
    private val socialProperties: SocialClientProperties,
    private val restTemplate: RestTemplate,
) {

    fun getSocialToken(code: String, socialProvider: SocialProvider, state: String?): OauthTokenResponse {
        val property = getPropertyForSocial(socialProvider.value)
        val oauthTokenResponse = WebClient.create()
            .post()
            .uri(property.tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(tokenRequest(code, property, socialProvider, state))
            .retrieve()
            .bodyToMono(OauthTokenResponse::class.java)
            .block()!!
        return oauthTokenResponse
    }

    fun getSocialUserInfo(socialProvider: SocialProvider, oauthTokenResponse: OauthTokenResponse, state: String?): Map<String, Any> {
        val map: Map<String, Any>
        when (socialProvider) {
            SocialProvider.KAKAO -> {
                map = WebClient.create()
                    .get()
                    .uri(socialProperties.kakao.userInfoUri)
                    .headers { headers ->
                        headers.setBearerAuth(oauthTokenResponse.accessToken!!)
                    }
                    .retrieve()
                    .bodyToMono(object : ParameterizedTypeReference<Map<String, Any>>() {})
                    .block()!!
            }
            SocialProvider.NAVER -> {
                map = WebClient.create()
                    .get()
                    .uri(socialProperties.naver.userInfoUri)
                    .headers { headers ->
                        headers.setBearerAuth(oauthTokenResponse.accessToken)
                    }
                    .retrieve()
                    .bodyToMono(object : ParameterizedTypeReference<Map<String, Any>>() {})
                    .block()!!
            }
            else -> {
                throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
            }
        }
        return map
    }

    fun sendRevokeRequest(socialAuthToken: SocialAuthToken, provider: SocialProvider) {
        verifySocialToken(socialAuthToken, provider)
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val revokeUrl = when (provider) {
            SocialProvider.KAKAO -> {
                httpHeaders.setBearerAuth(socialAuthToken.socialAccessToken)
                socialProperties.kakaoRevokeUrl()
            }
            SocialProvider.NAVER -> {
                socialProperties.naverRevokeUrl(socialAuthToken.socialAccessToken, SocialProvider.NAVER.value)
            }
            SocialProvider.GOOGLE -> {
                socialProperties.googleRevokeUrl(socialAuthToken.socialAccessToken)
            }
        }
        val httpEntity: HttpEntity<String> = HttpEntity(revokeUrl, httpHeaders)
        val response = restTemplate.exchange(revokeUrl, HttpMethod.POST, httpEntity, String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw IllegalArgumentException("회원 탈퇴에 실패하였습니다. 다시 시도해주십시오.")
        }
    }

    fun verifySocialToken(socialAuthToken: SocialAuthToken, provider: SocialProvider): SocialAuthToken {
        val now = LocalDateTime.now()
        val oauthTokenResponse: OauthTokenResponse

        if (now.isAfter(socialAuthToken.socialAccessTokenExpiresIn)) {
            val property = getPropertyForSocial(provider.value)
            val bodyValues = LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "refresh_token")
                add("client_id", property.clientId)
                add("refresh_token", socialAuthToken.socialRefreshToken)
            }

            oauthTokenResponse = WebClient.create()
                .post()
                .uri(property.tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(bodyValues))
                .retrieve()
                .bodyToMono(OauthTokenResponse::class.java)
                .block()!!

            socialAuthToken.updateAccessToken(oauthTokenResponse.accessToken!!)
            socialAuthToken.updateAccessTokenExpiresIn(oauthTokenResponse.expiresIn)
            if (provider != SocialProvider.NAVER && now.isAfter(socialAuthToken.socialRefreshTokenExpiresIn!!.minusDays(30))) {
                socialAuthToken.updateRefreshToken(oauthTokenResponse.refreshToken!!)
                socialAuthToken.updateRefreshTokenExpiresIn(oauthTokenResponse.refreshTokenExpiresIn!!)
            }
        }

        return socialAuthToken
    }

    private fun getPropertyForSocial(social: String) = when (social) {
        SocialProvider.KAKAO.value -> socialProperties.kakao
        SocialProvider.NAVER.value -> socialProperties.naver
        else -> throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
    }

    private fun tokenRequest(code: String, property: SocialClientProperties.Social, provider: SocialProvider, state: String?): MultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        when (provider) {
            SocialProvider.KAKAO -> {
                formData.add("grant_type", "authorization_code")
                formData.add("client_id", property.clientId)
                formData.add("redirect_uri", property.redirectUri)
                formData.add("code", code)
            }
            SocialProvider.NAVER -> {
                formData.add("grant_type", "authorization_code")
                formData.add("client_id", property.clientId)
                formData.add("client_secret", property.clientSecret)
                formData.add("code", code)
                formData.add("state", state)
            }
            else -> {
                throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
            }
        }
        return formData
    }
}
