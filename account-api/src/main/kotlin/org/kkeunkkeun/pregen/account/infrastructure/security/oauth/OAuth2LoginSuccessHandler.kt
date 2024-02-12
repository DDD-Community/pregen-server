package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenUtil: JwtTokenUtil,
    private val accountService: AccountService,
    private val refreshTokenService: RefreshTokenService,
): SimpleUrlAuthenticationSuccessHandler() {

    @Override
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val oAuthUser: OAuth2User = authentication.principal as OAuth2User

        val email = oAuthUser.attributes["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다.")
        val provider = oAuthUser.attributes["provider"] as? String ?: throw IllegalArgumentException("provider가 존재하지 않습니다.")
        val isExist: Boolean = oAuthUser.attributes["exist"] as? Boolean ?: throw IllegalArgumentException("exist가 존재하지 않습니다.")
        val accessToken = oAuthUser.attributes["accessToken"] as? String ?: throw IllegalArgumentException("accessToken이 존재하지 않습니다.")
        val nickName = oAuthUser.attributes["nickName"] as? String ?: accountService.generatedNickName()
        val role = oAuthUser.authorities.stream().findFirst().orElseThrow { throw IllegalArgumentException() }.authority

        if (isExist) {
            // 회원이 존재한다면, token 발행
            val account = accountService.findByEmail(email) ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
            account.updateAccessToken(accessToken)
            account.updateEmail(email)
            // accessToken을 가지고 있는 유저인지 확인
            refreshTokenService.deleteById(email)
            val jwtToken = jwtTokenUtil.generateToken(account.email, account.role.value)
            val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", jwtToken.accessToken)
            val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", jwtToken.refreshToken)

            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
            response.sendRedirect("/")
        } else {
            // 회원이 존재하지 않는다면, 회원가입 후 token 발행
            val account = accountService.signUp(email, nickName, provider, role, accessToken)
            val jwtToken = jwtTokenUtil.generateToken(account.email, account.role.value)
            val accessTokenCookie = jwtTokenUtil.generateTokenCookie("accessToken", jwtToken.accessToken)
            val refreshTokenCookie = jwtTokenUtil.generateTokenCookie("refreshToken", jwtToken.refreshToken)

            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
            response.sendRedirect("/")
        }
    }
}
