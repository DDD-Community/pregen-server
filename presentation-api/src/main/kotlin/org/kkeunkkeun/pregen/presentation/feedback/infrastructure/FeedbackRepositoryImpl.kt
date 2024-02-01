package org.kkeunkkeun.pregen.presentation.feedback.infrastructure

import org.kkeunkkeun.pregen.presentation.feedback.domain.Feedback
import org.kkeunkkeun.pregen.presentation.feedback.service.FeedbackRepository
import org.springframework.stereotype.Repository

@Repository
class FeedbackRepositoryImpl(
    private val feedbackJpaRepository: FeedbackJpaRepository
): FeedbackRepository {
    override fun findByPracticeId(practiceId: Long): Feedback? {
        return feedbackJpaRepository.findByPracticeId(practiceId)
    }
}