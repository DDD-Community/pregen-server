package org.kkeunkkeun.pregen.account.service

import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.account.domain.Account
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.domain.SocialProvider
import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.domain.dto.NickName
import org.kkeunkkeun.pregen.account.infrastructure.AccountJpaRepository
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.dto.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenRepository
import org.kkeunkkeun.pregen.common.service.JsonConvertor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Transactional(readOnly = true)
@Service
class AccountService(
    private val accountJpaRepository: AccountJpaRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenUtil: JwtTokenUtil,
    private val jsonConvertor: JsonConvertor,
    private val accountProperties: AccountProperties,
) {

    @Transactional
    fun signUp(email: String, nickName: String, provider: String, role: String): Account {
        val account = Account(
            email = email,
            nickName = nickName,
            socialProvider = SocialProvider.isType(provider),
            role = AccountRole.isType(role)
        )
        return accountJpaRepository.save(account)
    }

    @Transactional
    fun logoutAccount(request: HttpServletRequest) {
        if(SecurityContextHolder.getContext().authentication.principal.equals("anonymousUser")) {
            throw IllegalArgumentException("로그인 상태가 아닙니다.")
        }
        val accessToken = jwtTokenUtil.extractToken(request.getHeader("accessToken"))
            ?: throw IllegalArgumentException("accessToken이 존재하지 않습니다.")
        refreshTokenRepository.deleteById(accessToken)
    }

    @Transactional
    fun reIssueToken(request: HttpServletRequest): JwtTokenResponse {
        val refreshToken = jwtTokenUtil.extractToken(request.getHeader("refreshToken"))
            ?: throw IllegalArgumentException("refreshToken이 존재하지 않습니다.")
        jwtTokenUtil.verifyToken(refreshToken)
        val authentication = jwtTokenUtil.getAuthentication(refreshToken)
        val authorities = authentication.authorities.joinToString(",") { it.authority }

        return jwtTokenUtil.generateToken(authentication.name, authorities)
    }

    fun getMyAccount(email: String): AccountResponse {
        val account = accountJpaRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    @Transactional
    fun updateMyAccount(email: String, request: AccountUpdateRequest): AccountResponse {
        val account = accountJpaRepository.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        account.updateNickName(request.nickName)
        return AccountResponse(
            email = account.email,
            nickName = account.nickName,
            socialProvider = account.socialProvider.value,
        )
    }

    fun findByEmail(email: String): Account? {
        return accountJpaRepository.findByEmail(email)
    }

    fun generatedNickName(): String {
        val jsonContent = File(accountProperties.nameJson).readText()
        val nickName = jsonConvertor.readValue(jsonContent, NickName::class.java)
        return "${nickName.first.random().name} ${nickName.last.random().name}"
    }
}
