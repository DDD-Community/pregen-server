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
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.kkeunkkeun.pregen.account.infrastructure.security.oauth.OAuth2Attribute
import org.kkeunkkeun.pregen.account.oauth.domain.SocialAuthToken
import org.kkeunkkeun.pregen.account.oauth.service.OAuthService
import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.kkeunkkeun.pregen.common.service.JsonConvertor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class AccountService(
    private val accountRepository: AccountRepository,
    private val refreshTokenService: RefreshTokenService,
    private val oauthService: OAuthService,

    private val jwtTokenUtil: JwtTokenUtil,
    private val jsonConvertor: JsonConvertor,

    private val accountProperties: AccountProperties,
) {

    fun signUp(email: String, nickName: String, provider: String, role: String, socialToken: OauthTokenResponse): Account {
        val account = Account(
            email = email,
            nickName = nickName,
            socialProvider = SocialProvider.isType(provider),
            role = AccountRole.isType(role),
            socialAuthToken = SocialAuthToken(
                socialAccessToken = socialToken.accessToken,
                socialRefreshToken = socialToken.refreshToken!!,
                socialAccessTokenExpiresIn = LocalDateTime.now().plusSeconds(socialToken.expiresIn.toLong()),
                socialRefreshTokenExpiresIn = if (provider == SocialProvider.NAVER.value) null else LocalDateTime.now().plusSeconds(socialToken.refreshTokenExpiresIn!!.toLong())
            ),
            sessionId = UUID.randomUUID().toString(),
        )
        return accountRepository.save(account)
    }

    @Transactional
    fun loginAccount(code: String, provider: String, state: String?,
                     response: HttpServletResponse) {
        val providerType = SocialProvider.isType(provider)
        val socialAuthToken = oauthService.getSocialToken(code, providerType, state)
        val userInfo = oauthService.getSocialUserInfo(providerType, socialAuthToken, state)
        val attribute = OAuth2Attribute.of(providerType, userInfo, generatedNickName())

        var account = accountRepository.findByEmail(attribute.email)
        if (account != null) {
            account.updateAuthProfile(attribute.email, socialAuthToken)
        } else {
            account = signUp(attribute.email, attribute.nickName, provider, AccountRole.MEMBER.value, socialAuthToken)
        }

        val (accessToken, refreshToken) = jwtTokenUtil.generateToken(account.email, AccountRole.MEMBER.value)
        val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", accessToken)
        val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", refreshToken)
        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)
    }

    @Transactional
    fun logoutAccount(request: HttpServletRequest, email: String) {
        val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
        jwtTokenUtil.verifyToken(accessToken)
        refreshTokenService.deleteById(email)
    }

    @Transactional
    fun revokeAccount(request: HttpServletRequest, email: String) {
        val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
        jwtTokenUtil.verifyToken(accessToken)
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        oauthService.sendRevokeRequest(account.socialAuthToken, account.socialProvider)
        deleteMyAccount(account)
        refreshTokenService.deleteById(email)
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

    @Transactional
    fun updateAccountSessionId(sessionId: String) {
        val account = accountRepository.findBySessionId(sessionId) ?: throw PregenException(ErrorStatus.DATA_NOT_FOUND)
        account.updateSessionId(sessionId)
    }

    @Transactional
    fun reIssueToken(request: HttpServletRequest, response: HttpServletResponse, email: String) {
        val refreshToken = jwtTokenUtil.getTokenFromCookie("refreshToken", request)
        jwtTokenUtil.verifyToken(refreshToken)
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        val reissueSocialToken =
            oauthService.verifyAndReissueSocialToken(account.socialAuthToken, account.socialProvider)
        account.updateSocialAuthToken(reissueSocialToken)

        val authentication = jwtTokenUtil.getAuthentication(refreshToken)
        val authorities = authentication.authorities.joinToString(",") { it.authority }

        val jwtToken = jwtTokenUtil.generateToken(authentication.name, authorities)
        val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", jwtToken.accessToken)
        val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", jwtToken.refreshToken)

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)
    }

    fun getMyAccount(email: String): AccountResponse {
        val account = accountRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    fun generatedNickName(): String {
        val jsonContent = File(accountProperties.nameJson).readText()
        val nickName = jsonConvertor.readValue(jsonContent, NickName::class.java)
        return "${nickName.first.random().name} ${nickName.last.random().name}"
    }
}
