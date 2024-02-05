package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenUtil: JwtTokenUtil,
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = request.getHeader("Authorization")
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response)
            return
        }

        if (!jwtTokenUtil.verifyToken(accessToken)) {
            throw IllegalArgumentException("유효하지 않은 토큰입니다.")
        } else {
            val authentication = jwtTokenUtil.getAuthentication(accessToken)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}