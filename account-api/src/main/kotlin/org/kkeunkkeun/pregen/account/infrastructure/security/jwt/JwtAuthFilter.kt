package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
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
        try {
            if (request.requestURI.startsWith("/login")) {
                filterChain.doFilter(request, response)
                return
            }

            val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
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
        } catch (e: Exception) {
            // 에러 발생 시, 클라이언트에게 에러 메시지를 전달. 이후 커스텀 예외 response로 변경
            val objectMapper = ObjectMapper()
            response.contentType = "application/json; charset=utf-8"
            response.characterEncoding = "UTF-8"
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.outputStream.write(
                objectMapper.writeValueAsBytes(mapOf("message" to e.message))
            )
        }

        filterChain.doFilter(request, response)
    }
}
