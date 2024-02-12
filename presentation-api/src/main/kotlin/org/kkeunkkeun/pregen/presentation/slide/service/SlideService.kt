package org.kkeunkkeun.pregen.presentation.slide.service

import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SlideService(
    private val slideRepository: SlideRepository,
) {

    fun findByPracticeId(practiceId: Long): List<Slide> {
        return slideRepository.findByPracticeId(practiceId)
    }

    @Transactional
    fun saveAll(practiceId: Long, slideRequests: List<PresentationRequest.SlideRequest>) {
        val slides = slideRequests.map { request -> Slide.from(practiceId, null, request) } // TODO: add file

        slideRepository.saveAll(slides)
    }

    @Transactional
    fun overwrite(practiceId: Long, slideRequests: List<PresentationRequest.SlideRequest>) {
        this.findByPracticeId(practiceId)
            .forEach(Slide::unmap)

        this.saveAll(practiceId, slideRequests)
    }
}