package org.kkeunkkeun.pregen.presentation.slide.service

import org.kkeunkkeun.pregen.presentation.file.service.FileRepository
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SlideService(
    private val slideRepository: SlideRepository,
    private val fileRepository: FileRepository,
) {

    fun findByPracticeId(practiceId: Long): List<Slide> {
        return slideRepository.findByPracticeId(practiceId)
    }

    fun findAggregateByPracticeId(practiceId: Long): List<SlideAggregate> {
        return slideRepository.findAggregatesByPracticeId(practiceId)
    }

    @Transactional
    fun saveAll(practiceId: Long, slideRequests: List<PresentationRequest.SlideRequest>) {
        val slides = slideRequests.map { request -> mapToSlideEntity(practiceId, request) }

        slideRepository.saveAll(slides)
    }

    private fun mapToSlideEntity(practiceId: Long, slideRequest: PresentationRequest.SlideRequest): Slide {
        return Slide.from(
            practiceId = practiceId,
            imageFile = fileRepository.findById(slideRequest.imageFileId),
            request = slideRequest
        )
    }

    @Transactional
    fun overwrite(practiceId: Long, slideRequests: List<PresentationRequest.SlideRequest>) {
        this.findByPracticeId(practiceId)
            .forEach(Slide::unmap)

        this.saveAll(practiceId, slideRequests)
    }

    @Transactional
    fun doneSlide(email: String, slideId: Long, memo: String, audioFileId: Long?) {
        // TODO: add validation about account of slide

        val slide = slideRepository.findById(slideId)
            ?: throw IllegalStateException("Slide with id = `$slideId` does not exist.")

        val audioFile = fileRepository.findById(audioFileId)

        slide.done(memo, audioFile)
    }
}
