package org.kkeunkkeun.pregen.presentation.presentation.service

import org.kkeunkkeun.pregen.presentation.practice.service.PracticeRepository
import org.kkeunkkeun.pregen.presentation.slide.service.SlideRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PresentationThumbnailResolver(
    private val practiceRepository: PracticeRepository,
    private val slideRepository: SlideRepository,
) {

    fun findThumbnail(presentationId: Long): String? {
        return practiceRepository.findLatestByPresentationId(presentationId)
            .let { practice ->
                slideRepository.findFirstByPracticeId(practice.id!!)
            }?.imageFilePath
    }
}