package org.kkeunkkeun.pregen.account.presentation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.kkeunkkeun.pregen.account.domain.dto.AccountResponse
import org.kkeunkkeun.pregen.account.domain.dto.AccountUpdateRequest
import org.kkeunkkeun.pregen.account.presentation.annotation.AccountEmail
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,
) {

    @GetMapping("/login/process")
    fun login(
        @NotNull @RequestParam("code") code: String,
        @NotNull @RequestParam("provider") provider: String,
        @RequestParam("state") state: String?,
        response: HttpServletResponse): ResponseEntity<Any> {
        return ResponseEntity.ok().body(accountService.loginAccount(code, provider, state, response))
    }

    @GetMapping("/reissue")
    fun reissueToken(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Unit> {
        accountService.reIssueToken(request, response)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Unit> {
        accountService.logoutAccount(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/revoke")
    fun revokeToken(request: HttpServletRequest,
                    @AccountEmail email: String): ResponseEntity<Unit> {
        accountService.revokeAccount(request, email)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me")
    fun getMyAccount(@AccountEmail email: String): ResponseEntity<Any> {
        return ResponseEntity.ok().body(accountService.getMyAccount(email))
    }

    @PatchMapping("/me")
    fun updateMyAccount(@RequestBody @Valid request: AccountUpdateRequest,
                        @AccountEmail email: String): ResponseEntity<AccountResponse> {
        return ResponseEntity.ok().body(accountService.updateMyAccount(email, request))
    }
}
