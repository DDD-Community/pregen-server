package org.kkeunkkeun.pregen.account.presentation

import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,
) {

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.name
        accountService.logoutAccount(username)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/rotate")
    fun rotateToken(request: HttpServletRequest): ResponseEntity<JwtTokenResponse> {
        val refreshToken = JwtTokenUtil.extractToken(request.getHeader("refreshToken"))
            ?: throw IllegalArgumentException("refreshToken이 존재하지 않습니다.")

        return ResponseEntity.ok().body(accountService.rotateToken(refreshToken))
    }
}