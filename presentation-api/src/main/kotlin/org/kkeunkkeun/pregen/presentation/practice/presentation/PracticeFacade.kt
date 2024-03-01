package org.kkeunkkeun.pregen.presentation.practice.presentation

import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.kkeunkkeun.pregen.presentation.memorizationsentence.service.MemorizationSentenceRepository
import org.kkeunkkeun.pregen.presentation.memorizationsentence.service.MemorizationSentenceService
import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.kkeunkkeun.pregen.presentation.practice.service.PracticeService
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationService
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate
import org.kkeunkkeun.pregen.presentation.slide.service.SlideService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PracticeFacade(
    private val presentationService: PresentationService,
    private val practiceService: PracticeService,
    private val slideService: SlideService,
    private val accountRepository: AccountRepository,
    private val memorizationSentenceService: MemorizationSentenceService,
) {

    @Transactional
    fun startPractice(email: String, presentationId: Long): PracticeResponse {
        val presentation = presentationService.findById(email, presentationId)
        val practice = practiceService.findLatest(presentationId)

        practice.start()

        return getPracticeDetail(practice, email, presentation)
    }

    fun getPracticeDetail(email: String, presentationId: Long): PracticeResponse {
        val presentation = presentationService.findById(email, presentationId)
        val practice = practiceService.findLatest(presentationId)

        return getPracticeDetail(practice, email, presentation)
    }

    private fun getPracticeDetail(
        practice: Practice,
        email: String,
        presentation: Presentation,
    ): PracticeResponse {
        val slides = slideService.findAggregateByPracticeId(practice.id!!)

        val activateNextSlideModal = accountRepository.findByEmail(email)
            ?.activateNextSlideModal
            ?: throw IllegalStateException("user not found.")

        return PracticeResponse.from(
            presentation = presentation,
            practice = practice,
            slides = slides,
            activateNextSlideModal = activateNextSlideModal,
        )
    }

    @Transactional
    fun updatePracticeDetail(email: String, presentationId: Long, practiceRequest: PracticeRequest) {
        val practice = practiceService.findLatest(presentationId)

        practiceRequest.slides?.forEach { request ->
            memorizationSentenceService.overwrite(request.id, request.memorizationSentences)
        }

        practice.updateMode(practiceRequest.practiceMode)
    }
}