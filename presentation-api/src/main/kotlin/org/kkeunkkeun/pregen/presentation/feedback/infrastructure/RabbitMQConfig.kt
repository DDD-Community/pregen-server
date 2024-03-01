package org.kkeunkkeun.pregen.presentation.feedback.infrastructure

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun feedbackQueue() = Queue("pregen.feedback")
}