package org.kkeunkkeun.pregen.account.infrastructure.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenProvider
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val jwtTokenProvider: JwtTokenProvider
): HandlerInterceptor {

    @Override
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessToken: String? = JwtTokenUtil.extractToken(request.getHeader("Authorization"))

        if (accessToken != null) {
            if (jwtTokenProvider.validateAccessToken(accessToken)) {
                val authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        return true
    }

}
