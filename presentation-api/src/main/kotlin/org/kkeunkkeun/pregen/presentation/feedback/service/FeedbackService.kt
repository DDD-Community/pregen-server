package org.kkeunkkeun.pregen.presentation.feedback.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.kkeunkkeun.pregen.presentation.feedback.domain.Feedback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper,
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun findByPracticeId(practiceId: Long): Feedback {
        return feedbackRepository.findByPracticeId(practiceId)
            ?: throw IllegalStateException()
    }

    // rabbitmq publisher
    fun publishFeedback(feedback: RabbitMqTestRequest) {
        val response = objectMapper.writeValueAsString(feedback)
        rabbitTemplate.convertAndSend("pregen.feedback", response)
    }

    data class RabbitMqTestRequest(
        val feedback: String,
        val message: String
    )
}