package org.kkeunkkeun.pregen.presentation.presentation.presentation

import jakarta.validation.Valid
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException

@RestController
@RequestMapping("/api/presentations")
class PresentationController(
    private val presentationService: PresentationService
) {

    @GetMapping
    fun getPresentationList(@PageableDefault pageable: Pageable): PresentationListResponse {
        val memberId: Long = 0

        return PresentationListResponse(
            presentationService.findListByMemberId(memberId, pageable)
        )
    }

    @GetMapping("/{presentationId}")
    fun getPresentation(@PathVariable presentationId: Long): Presentation {
//        return presentationService.findById(presentationId)
        throw RuntimeException()
    }

    @PostMapping
    fun registerPresentation(@Valid @RequestBody presentationRequest: PresentationRequest): Long {
        val memberId = 1L

        return presentationService.save(memberId, presentationRequest)
    }
}
