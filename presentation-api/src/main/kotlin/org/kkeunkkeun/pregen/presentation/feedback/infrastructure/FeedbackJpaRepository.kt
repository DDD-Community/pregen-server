package org.kkeunkkeun.pregen.presentation.feedback.infrastructure

import org.kkeunkkeun.pregen.presentation.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackJpaRepository: JpaRepository<Feedback, Long> {
    fun findByPracticeId(practiceId: Long): Feedback?
}