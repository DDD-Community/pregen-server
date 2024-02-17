package org.kkeunkkeun.pregen.account.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.domain.Account
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.domain.SocialProvider
import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.domain.dto.NickName
import org.kkeunkkeun.pregen.account.domain.dto.OauthTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.account.infrastructure.config.SocialClientProperties
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.kkeunkkeun.pregen.account.infrastructure.security.oauth.OAuth2Attribute
import org.kkeunkkeun.pregen.common.service.JsonConvertor
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.io.File
import java.util.*

@Transactional(readOnly = true)
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val refreshTokenService: RefreshTokenService,

    private val jwtTokenUtil: JwtTokenUtil,
    private val jsonConvertor: JsonConvertor,
    private val restTemplate: RestTemplate,

    private val accountProperties: AccountProperties,
    private val socialProperties: SocialClientProperties,
) {

    @Transactional
    fun signUp(email: String, nickName: String, provider: String, role: String, accessToken: String): Account {
        val account = Account(
            email = email,
            nickName = nickName,
            socialProvider = SocialProvider.isType(provider),
            role = AccountRole.isType(role),
            socialAccessToken = accessToken,
            sessionId = UUID.randomUUID().toString(),
        )
        return accountRepository.save(account)
    }

    @Transactional
    fun loginAccount(provider: String, code: String, response: HttpServletResponse) {
        when (provider) {
            SocialProvider.KAKAO.value -> {
                val socialAccessToken = getSocialToken(code, provider)
                val userInfo = getUserInfo(provider, socialAccessToken)
                val attribute = OAuth2Attribute.of(provider, "email", userInfo, generatedNickName())

                val account = accountRepository.findByEmail(attribute.email)
                if (account != null) {
                    account.updateEmail(attribute.email)
                } else {
                    signUp(attribute.email, attribute.email, provider, AccountRole.MEMBER.value, socialAccessToken.accessToken)
                }

                val (accessToken, refreshToken) = jwtTokenUtil.generateToken(attribute.email, AccountRole.MEMBER.value)
                val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", accessToken)
                val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", refreshToken)
                response.addCookie(accessTokenCookie)
                response.addCookie(refreshTokenCookie)
            }
        }
    }

    @Transactional
    fun logoutAccount(request: HttpServletRequest, authentication: Authentication) {
        if(authentication.principal.equals("anonymousUser")) {
            throw IllegalArgumentException("로그인 상태가 아닙니다.")
        }
        val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
        refreshTokenService.deleteById(authentication.name)
    }

    @Transactional
    fun reIssueToken(request: HttpServletRequest, response: HttpServletResponse): HttpServletResponse {
        val refreshToken = jwtTokenUtil.getTokenFromCookie("refreshToken", request)
        jwtTokenUtil.verifyToken(refreshToken)
        val authentication = jwtTokenUtil.getAuthentication(refreshToken)
        val authorities = authentication.authorities.joinToString(",") { it.authority }

        val jwtToken = jwtTokenUtil.generateToken(authentication.name, authorities)
        val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", jwtToken.accessToken)
        val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", jwtToken.refreshToken)

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)
        return response
    }

    fun revokeAccount(request: HttpServletRequest, email: String) {
        val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
        jwtTokenUtil.verifyToken(accessToken)
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        sendRevokeRequest(account.socialAccessToken, account.socialProvider)
        deleteMyAccount(account)
        refreshTokenService.deleteById(email)
    }


    fun getMyAccount(email: String): AccountResponse {
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    @Transactional
    fun updateMyAccount(email: String, request: AccountUpdateRequest): AccountResponse {
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        account.updateNickName(request.nickName)
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    @Transactional
    fun deleteMyAccount(account: Account) {
        accountRepository.delete(account)
    }

    fun findByEmail(email: String): Account? {
        return accountRepository.findByEmail(email)
    }

    fun generatedNickName(): String {
        val jsonContent = File(accountProperties.nameJson).readText()
        val nickName = jsonConvertor.readValue(jsonContent, NickName::class.java)
        return "${nickName.first.random().name} ${nickName.last.random().name}"
    }

    private fun getSocialToken(code: String, social: String): OauthTokenResponse {
        val oauthTokenResponse: OauthTokenResponse
        when (social) {
            SocialProvider.KAKAO.value -> {
                oauthTokenResponse = WebClient.create()
                    .post()
                    .uri(socialProperties.kakaoTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(tokenRequest(code, socialProperties))
                    .retrieve()
                    .bodyToMono(OauthTokenResponse::class.java)
                    .block()!!
            }

            else -> {
                throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
            }
        }
        return oauthTokenResponse
    }

    private fun getUserInfo(provider: String, oauthTokenResponse: OauthTokenResponse): Map<String, Any> {
        var map: Map<String, Any> = mapOf()
        when (provider) {
            SocialProvider.KAKAO.value -> {
                map = WebClient.create()
                    .get()
                    .uri(socialProperties.kakao.userInfoUri)
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

    private fun tokenRequest(code: String, socialProperties: SocialClientProperties): MultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("grant_type", "authorization_code")
        formData.add("client_id", socialProperties.kakao.clientId)
        formData.add("redirect_uri", socialProperties.kakao.redirectUri)
        formData.add("code", code)
        return formData
    }

    private fun sendRevokeRequest(accessToken: String, provider: SocialProvider) {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val revokeUrl = when (provider) {
            SocialProvider.KAKAO -> {
                httpHeaders.setBearerAuth(accessToken)
                socialProperties.kakaoRevokeUrl()
            }
            SocialProvider.NAVER -> {
                socialProperties.naverRevokeUrl(accessToken, SocialProvider.NAVER.value)
            }
            SocialProvider.GOOGLE -> {
                socialProperties.googleRevokeUrl(accessToken)
            }
        }
        val httpEntity: HttpEntity<String> = HttpEntity(revokeUrl, httpHeaders)
        val response = restTemplate.exchange(revokeUrl, HttpMethod.POST, httpEntity, String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw IllegalArgumentException("회원 탈퇴에 실패하였습니다. 다시 시도해주십시오.")
        }
    }
}
