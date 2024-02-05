package org.kkeunkkeun.pregen.presentation.practice.service

import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PracticeService(
    private val practiceRepository: PracticeRepository,
) {

    fun findLatest(presentationId: Long): Practice {
        return practiceRepository.findLatestByPresentationId(presentationId)
    }

    @Transactional
    fun save(presentation: Presentation): Long {
        val practice = Practice(null, presentation.id!!, presentation.title, presentation.timeLimit, 0)

        practiceRepository.save(practice)

        return practice.id!!
    }
}