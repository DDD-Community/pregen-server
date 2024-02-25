package org.kkeunkkeun.pregen.presentation.practice.presentation

import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.presentation.annotation.AccountEmail
import org.kkeunkkeun.pregen.account.service.AccountService
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/practices")
class PracticeController(
    private val practiceFacade: PracticeFacade,
    private val accountService: AccountService,
) {

    @GetMapping("/presentation/{presentationId}")
    fun getPracticeDetail(
        @AccountEmail email: String,
        @PathVariable presentationId: Long,
    ): ResponseEntity<PracticeResponse> {
        val result = practiceFacade.getPracticeDetail(email, presentationId)

        return ResponseEntity.status(OK)
            .body(result)
    }

    @PatchMapping("/presentation/{presentationId}")
    fun updatePractice(
        @AccountEmail email: String,
        @PathVariable presentationId: Long,
        @Valid @RequestBody practiceRequest: PracticeRequest,
    ): ResponseEntity<Void> {
        practiceFacade.updatePracticeDetail(email, presentationId, practiceRequest)

        return ResponseEntity.status(OK)
            .build()
    }

    @PatchMapping("/deactivate-modal")
    fun deactivateModal(@AccountEmail email: String): ResponseEntity<Void> {
        accountService.deactivateNextSlideModal(email)

        return ResponseEntity.status(OK)
            .build()
    }
}