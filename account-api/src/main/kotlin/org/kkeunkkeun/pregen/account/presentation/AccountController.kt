package org.kkeunkkeun.pregen.account.presentation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService,
) {

    @GetMapping("/login/process")
    fun login(
        @RequestParam("code") code: String,
        @RequestParam("provider") provider: String, response: HttpServletResponse): ResponseEntity<Any> {
        return ResponseEntity.ok().body(accountService.loginAccount(provider, code, response))
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Unit> {
        val authentication = SecurityContextHolder.getContext().authentication
        accountService.logoutAccount(request, authentication)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/reissue")
    fun reissueToken(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<HttpServletResponse> {
        return ResponseEntity.ok().body(accountService.reIssueToken(request, response))
    }

    @GetMapping("/revoke")
    fun revokeToken(request: HttpServletRequest): ResponseEntity<Unit> {
        val email = SecurityContextHolder.getContext().authentication.name
        accountService.revokeAccount(request, email)
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
