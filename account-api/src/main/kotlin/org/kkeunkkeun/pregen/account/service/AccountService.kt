package org.kkeunkkeun.pregen.account.service

import lombok.RequiredArgsConstructor
import org.kkeunkkeun.pregen.account.domain.Account
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.domain.dto.AccountSaveRequest
import org.kkeunkkeun.pregen.account.infrastructure.AccountRepository
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenProvider
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Transactional(readOnly = true)
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtTokenUtil: JwtTokenUtil,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
) {

//    @Transactional
//    fun joinAccount(request: AccountSaveRequest) {
//        accountRepository.findByEmail(request.email)
//            .ifPresent { throw IllegalArgumentException("이미 존재하는 이메일입니다.") }
//
//        Account.of(request, request.nickName ?: generatedNickName(), AccountRole.MEMBER)
//            .let { entity ->
//                accountRepository.save(entity)
//            }
//    }

    fun logoutAccount(refreshToken: String) {
        jwtTokenUtil.deleteRefreshToken(refreshToken)
    }

    fun rotateToken(prevRefreshToken: String): JwtTokenResponse {
        jwtTokenProvider.validateRefreshToken(prevRefreshToken)
        val authentication = jwtTokenProvider.getAuthenticationByAccessToken(prevRefreshToken)

        val authorities = authentication.authorities.joinToString(",") { it.authority }
        val jwtTokenResponse = jwtTokenProvider.createdJwtToken(authentication.name, authorities)
        jwtTokenUtil.rotateRefreshToken(prevRefreshToken, jwtTokenResponse.refreshToken, authentication.name)

        return jwtTokenResponse
    }

    private fun generatedNickName(): String {
        val jsonContent = File("account-api/src/main/resources/names/names.json").readText()

        val firstNames = extractNames(jsonContent, "first")
        val lastNames = extractNames(jsonContent, "last")

        val randomNickName = "${firstNames.random()} ${lastNames.random()}"
        return randomNickName
    }

    private fun extractNames(json: String, key: String): List<String> {
        val regexPattern = """"$key":\s*\[((?:\s*\{\s*"name":\s*"[^"]*"\s*},?\s*)*)]""".toRegex()
        val namesMatchResult = regexPattern.find(json)

        val nameRegex = """"name":\s*"([^"]*)"""".toRegex()

        return namesMatchResult?.let {
            nameRegex.findAll(it.value).map { matchResult ->
                matchResult.groupValues[1]
            }.toList()
        } ?: throw IllegalArgumentException("무작위 닉네임 생성에 필요한 이름 목록이 존재하지 않습니다.")
    }
}