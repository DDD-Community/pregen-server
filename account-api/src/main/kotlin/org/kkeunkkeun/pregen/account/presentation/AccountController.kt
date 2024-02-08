package org.kkeunkkeun.pregen.account.presentation

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.dto.JwtTokenResponse
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService,
) {

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Unit> {
        accountService.logoutAccount(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/reissue")
    fun reissueToken(request: HttpServletRequest): ResponseEntity<JwtTokenResponse> {
        return ResponseEntity.ok().body(accountService.reIssueToken(request))
    }

    @GetMapping("/revoke")
    fun revokeToken(request: HttpServletRequest): ResponseEntity<Unit> {
        accountService.revokeAccount(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me")
    fun getMyAccount(): ResponseEntity<Any> {
        val email = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(accountService.getMyAccount(email))
    }

    @PatchMapping("/me")
    fun updateMyAccount(@RequestBody @Valid request: AccountUpdateRequest): ResponseEntity<AccountResponse> {
        val email = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(accountService.updateMyAccount(email, request))
    }
}
