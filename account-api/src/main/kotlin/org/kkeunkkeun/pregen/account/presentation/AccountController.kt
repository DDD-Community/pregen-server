package org.kkeunkkeun.pregen.account.presentation

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtTokenUtil
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService,
    private val jwtTokenUtil: JwtTokenUtil,
) {

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Any> {
        val accessToken = jwtTokenUtil.getAccessToken(request)
        val refreshToken = jwtTokenUtil.getRefreshToken(request)

        accountService.logoutAccount(accessToken, refreshToken)
        return ResponseEntity.ok().build()
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rotate")
    fun rotateToken(request: HttpServletRequest): ResponseEntity<JwtTokenResponse> {
        val refreshToken = JwtTokenUtil.extractToken(request.getHeader("refreshToken"))
            ?: throw IllegalArgumentException("refreshToken이 존재하지 않습니다.")
        return ResponseEntity.ok().body(accountService.reIssueTokens(refreshToken))
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    fun getMyAccount(): ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(accountService.getMyAccount(username))
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    fun updateMyAccount(@RequestBody @Valid request: AccountUpdateRequest): ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(accountService.updateMyAccount(username, request))
    }
}