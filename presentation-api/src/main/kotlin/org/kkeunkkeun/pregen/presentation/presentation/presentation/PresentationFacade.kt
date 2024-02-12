package org.kkeunkkeun.pregen.presentation.presentation.presentation

import org.kkeunkkeun.pregen.presentation.practice.service.PracticeService
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationService
import org.kkeunkkeun.pregen.presentation.slide.service.SlideService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PresentationFacade(
    private val presentationService: PresentationService,
    private val practiceService: PracticeService,
    private val slideService: SlideService,
) {

    fun readPresentationDetail(email: String, presentationId: Long): PresentationResponse.PresentationDetail {
        val presentation = presentationService.findById(email, presentationId)
        val practice = practiceService.findLatest(presentationId)
        val slides = slideService.findByPracticeId(practice.id!!)

        return PresentationResponse.PresentationDetail.from(presentation, slides)
    }

    @Transactional
    fun persist(email: String, request: PresentationRequest): Long {
        val presentation = presentationService.save(email, request)
        val practiceId = practiceService.save(presentation)
        slideService.saveAll(practiceId, request.slides)

        return presentation.id!!
    }

    @Transactional
    fun update(email: String, presentationId: Long, request: PresentationRequest): Long {
        val updatedPresentationId = presentationService.update(email, presentationId, request)
        val practiceId = practiceService.findLatest(updatedPresentationId).id ?: throw RuntimeException()

        slideService.overwrite(practiceId, request.slides)

        return updatedPresentationId
    }
}