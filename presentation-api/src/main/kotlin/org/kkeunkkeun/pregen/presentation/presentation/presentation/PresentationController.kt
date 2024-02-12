package org.kkeunkkeun.pregen.presentation.presentation.presentation

import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.presentation.annotation.AccountEmail
import org.kkeunkkeun.pregen.common.presentation.CustomPage
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/presentations")
class PresentationController(
    private val presentationService: PresentationService,
    private val presentationFacade: PresentationFacade,
) {

    @GetMapping
    fun getPresentationList(
        @AccountEmail email: String,
        @PageableDefault pageable: Pageable,
    ): ResponseEntity<PresentationListResponse> {
        val page = presentationService.findListByAccountId(email, pageable)
        val responseBody = PresentationListResponse(CustomPage.from(page))

        return ResponseEntity.status(OK)
            .body(responseBody)
    }

    @GetMapping("/{presentationId}")
    fun getPresentation(
        @AccountEmail email: String,
        @PathVariable presentationId: Long,
    ): ResponseEntity<PresentationResponse.PresentationDetail> {
        val responseBody = presentationFacade.readPresentationDetail(email, presentationId)

        return ResponseEntity.status(OK)
            .body(responseBody)
    }

    @PostMapping
    fun registerPresentation(
        @AccountEmail email: String,
        @Valid @RequestBody presentationRequest: PresentationRequest,
    ): ResponseEntity<PresentationResponse.PresentationPersist> {
        val responseBody = PresentationResponse.PresentationPersist(
            presentationFacade.persist(email, presentationRequest))

        return ResponseEntity.status(CREATED)
            .body(responseBody)
    }

    @PatchMapping("/{presentationId}")
    fun updatePresentation(
        @AccountEmail email: String,
        @PathVariable presentationId: Long,
        @Valid @RequestBody presentationRequest: PresentationRequest,
    ): ResponseEntity<PresentationResponse.PresentationPersist> {
        val responseBody = PresentationResponse.PresentationPersist(
            presentationFacade.update(email, presentationId, presentationRequest))

        return ResponseEntity.status(OK)
            .body(responseBody)
    }

    @DeleteMapping
    fun deletePresentation(
        @AccountEmail email: String,
        @Valid @RequestBody deleteBody: PresentationRequest.Delete,
    ): ResponseEntity<Void> {
        presentationService.deleteByIds(email, deleteBody.presentationIds)

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .build()
    }
}
