package org.kkeunkkeun.pregen.presentation.feedback.service

import org.kkeunkkeun.pregen.presentation.feedback.domain.Feedback
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository
) {

    fun findByPracticeId(practiceId: Long): Feedback {
        return feedbackRepository.findByPracticeId(practiceId)
            ?: throw IllegalStateException()
    }
}