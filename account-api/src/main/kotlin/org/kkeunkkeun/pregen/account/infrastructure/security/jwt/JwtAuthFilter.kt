package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.kkeunkkeun.pregen.common.presentation.ErrorResponse
import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val objectMapper: ObjectMapper,

    private val refreshTokenService: RefreshTokenService,
): OncePerRequestFilter() {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (request.requestURI.contains("login")) {
                val isToken = jwtTokenUtil.checkTokenFromCookie("accessToken", request)
                if (isToken) {
                    throw PregenException(ErrorStatus.UNAUTHORIZED, "이미 로그인된 상태입니다.")
                }
                filterChain.doFilter(request, response)
                return
            }

            if (request.requestURI.contains("reissue")) {
                val refreshToken = jwtTokenUtil.getTokenFromCookie("refreshToken", request)
                if (!StringUtils.hasText(refreshToken)) {
                    throw PregenException(ErrorStatus.UNAUTHORIZED, "헤더에 토큰이 존재하지 않습니다.")
                }

                if (!jwtTokenUtil.verifyToken(refreshToken) && !refreshTokenService.verifyToken("refreshToken", refreshToken)) {
                    throw PregenException(ErrorStatus.INVALID_TOKEN, "유효하지 않은 토큰입니다.")
                } else {
                    val authentication = jwtTokenUtil.getAuthentication(refreshToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }

                filterChain.doFilter(request, response)
                return
            } else {
                val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
                if (!StringUtils.hasText(accessToken)) {
                    throw PregenException(ErrorStatus.UNAUTHORIZED, "헤더에 토큰이 존재하지 않습니다.")
                }

                if (!jwtTokenUtil.verifyToken(accessToken) && !refreshTokenService.verifyToken("accessToken", accessToken)) {
                    throw PregenException(ErrorStatus.INVALID_TOKEN, "유효하지 않은 토큰입니다.")
                } else {
                    val authentication = jwtTokenUtil.getAuthentication(accessToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }

                filterChain.doFilter(request, response)
            }
        } catch (e: PregenException) {
            log.error("Filter error: ${e.message}")
            handleException(request, response, e)
        } catch (e: Exception) {
            log.error("Filter error: ${e.message}")
            handleException(request, response, e, "인증 과정에서 예외가 발생했습니다.")
        }
    }

    private fun handleException(request: HttpServletRequest, response: HttpServletResponse, e: Exception, message: String? = null) {
        if (e is PregenException) {
            val errorResponse = ErrorResponse.from(e, request.requestURI)
            response.status = e.errorStatus.status.value()
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.outputStream.write(objectMapper.writeValueAsBytes(errorResponse))
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            val errorResponse = ErrorResponse.from(
                PregenException(ErrorStatus.UNAUTHORIZED, message ?: "예외가 발생했습니다."),
                request.requestURI
            )
            response.outputStream.write(
                objectMapper.writeValueAsBytes(errorResponse)
            )
        }
    }
}
