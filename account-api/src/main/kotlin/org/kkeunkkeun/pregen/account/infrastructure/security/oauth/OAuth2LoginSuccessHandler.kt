package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.domain.Account
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.domain.SocialProvider
import org.kkeunkkeun.pregen.account.infrastructure.AccountRepository
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenProvider
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OAuth2LoginSuccessHandler(
    private val accountRepository: AccountRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
): SimpleUrlAuthenticationSuccessHandler() {

    @Transactional
    @Override
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val oAuthUser: OAuth2User = authentication.principal as OAuth2User

        val email = oAuthUser.attributes["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다.")
        val findAccount = accountRepository.findByEmail(email)
        val name = oAuthUser.attributes["nickName"] as String
        val oauthToken = authentication as OAuth2AuthenticationToken

        val loginAccount = if (findAccount.isEmpty) {
            // 로그인을 시도했으나, 회원가입이 되어있지 않은 경우
            val newAccount = Account(
                email = email,
                nickName = name,
                socialProvider = SocialProvider.isType(oauthToken.authorizedClientRegistrationId),
                role = AccountRole.MEMBER,
            )
            accountRepository.save(newAccount)
        } else {
            // 로그인을 시도했으며, 회원가입이 되어있는 경우
            val account = findAccount.orElseThrow { NotFoundException() }
            account.generatedSocialAccount(email, name) // 소셜 계정의 정보를 최신화
        }

        val jwtTokenResponse = jwtTokenProvider.createdJwtToken(loginAccount.email, loginAccount.role.value)
        response.contentType = "application/json; charset=utf-8"
        response.characterEncoding = "UTF-8"
        response.status = 200
        response.writer.write(
            objectMapper.writeValueAsString(jwtTokenResponse)
        )
    }
}
