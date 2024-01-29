package org.kkeunkkeun.pregen.account.service

import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.infrastructure.AccountRepository
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenProvider
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtTokenUtil: JwtTokenUtil,
) {

    fun logoutAccount(accessToken: String, refreshToken: String) {
        if(SecurityContextHolder.getContext().authentication.principal.equals("anonymousUser")) {
            throw IllegalArgumentException("로그인 상태가 아닙니다.")
        }
        jwtTokenUtil.blockAccessToken(accessToken)
        jwtTokenUtil.deleteRefreshToken(refreshToken)
    }

    fun reIssueTokens(prevRefreshToken: String): JwtTokenResponse {
        jwtTokenProvider.validateRefreshToken(prevRefreshToken)
        val authentication = jwtTokenProvider.getAuthenticationByAccessToken(prevRefreshToken)

        val authorities = authentication.authorities.joinToString(",") { it.authority }
        val jwtTokenResponse = jwtTokenProvider.createdJwtToken(authentication.name, authorities)
        jwtTokenUtil.rotateRefreshToken(prevRefreshToken, jwtTokenResponse.refreshToken, authentication.name)

        return jwtTokenResponse
    }

    fun getMyAccount(username: String): AccountResponse {
        val account = accountRepository.findByEmail(username).orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    @Transactional
    fun updateMyAccount(username: String, request: AccountUpdateRequest): AccountResponse {
        val account = accountRepository.findByEmail(username).orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
        account.updateNickName(request.nickName)
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }
}
