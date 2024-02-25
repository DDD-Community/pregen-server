package org.kkeunkkeun.pregen.presentation.slide.presentation

import jakarta.validation.Valid
import org.kkeunkkeun.pregen.account.presentation.annotation.AccountEmail
import org.kkeunkkeun.pregen.presentation.slide.service.SlideService
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/slides")
class SlideController(
    private val slideService: SlideService,
) {

    @PatchMapping("/{slideId}")
    fun savePracticedSlide(
        @AccountEmail email: String,
        @PathVariable slideId: Long,
        @Valid @RequestBody slideRequest: SlideRequest,
    ): ResponseEntity<Void> {
        slideService.doneSlide(
            email = email,
            slideId = slideId,
            memo = slideRequest.memo,
            audioFileId = slideRequest.audioFileId)

        return ResponseEntity.status(OK)
            .build()
    }
}
