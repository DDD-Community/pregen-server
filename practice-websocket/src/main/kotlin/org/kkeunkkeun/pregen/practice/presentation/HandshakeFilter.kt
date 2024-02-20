package org.kkeunkkeun.pregen.practice.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.kkeunkkeun.pregen.common.presentation.ErrorResponse
import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.kkeunkkeun.pregen.practice.domain.Constant.Companion.SESSION_ID_HEADER_NAME
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class HandshakeFilter(
    private val accountRepository: AccountRepository,
    private val jwtTokenUtil: JwtTokenUtil,
    private val objectMapper: ObjectMapper,
): Filter {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (request !is HttpServletRequest || response !is HttpServletResponse) {
            log.error("Non-HTTP request or response.")
            return
        }

        try {
            val accessToken = jwtTokenUtil.getTokenFromCookie("accessToken", request)
            if (!StringUtils.hasText(accessToken)) {
                throw PregenException(ErrorStatus.UNAUTHORIZED, "헤더에 토큰이 존재하지 않습니다.")
            }

            if (!jwtTokenUtil.verifyToken(accessToken)) {
                throw PregenException(ErrorStatus.INVALID_TOKEN, "유효하지 않은 토큰입니다.")
            } else {
                val authentication = jwtTokenUtil.getAuthentication(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
            }

            val email = SecurityContextHolder.getContext().authentication.name
            val account = accountRepository.findByEmail(email)
                ?: throw IllegalStateException("Account with email $email not found.")

            // Directly use account.practiceSessionId without extra variable
            response.addHeader(SESSION_ID_HEADER_NAME, account.sessionId)

            chain?.doFilter(request, response)
        } catch (e: PregenException) {
            handleException(request, response, e, e.errorStatus.message ?: "예외가 발생했습니다.")
        } catch (e: Exception) {
            handleException(request, response, e, "예외가 발생했습니다.")
        }
    }

    private fun handleException(request: HttpServletRequest, response: HttpServletResponse, e: Exception, message: String) {
        if (e is PregenException) {
            val errorResponse = ErrorResponse.from(e, request.requestURI)
            response.status = HttpStatus.valueOf(errorResponse.code).value()
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"

            response.outputStream.write(objectMapper.writeValueAsBytes(errorResponse))
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            val errorResponse = mapOf("message" to message)
            response.outputStream.write(
                objectMapper.writeValueAsBytes(errorResponse)
            )
        }
    }
}
