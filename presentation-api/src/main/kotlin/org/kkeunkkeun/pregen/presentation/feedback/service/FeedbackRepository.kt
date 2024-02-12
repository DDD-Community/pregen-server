package org.kkeunkkeun.pregen.presentation.feedback.service

import org.kkeunkkeun.pregen.presentation.feedback.domain.Feedback

interface FeedbackRepository {

    fun findByPracticeId(practiceId: Long): Feedback?
}