package org.kkeunkkeun.pregen.practice.presentation

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.kkeunkkeun.pregen.practice.domain.Constant
import org.kkeunkkeun.pregen.practice.domain.Constant.Companion.SESSION_ID_HEADER_NAME
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class HandshakeFilter(
    private val accountRepository: AccountRepository
): Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            val email = SecurityContextHolder.getContext().authentication.name

            val account = accountRepository.findByEmail(email)
                ?: throw IllegalStateException("Account with email $email not found.")

            // Directly use account.practiceSessionId without extra variable
            response.addHeader(SESSION_ID_HEADER_NAME, account.sessionId)

            chain?.doFilter(request, response)

        } else {
            throw IllegalStateException("Non-HTTP request or response.")
        }
    }
}