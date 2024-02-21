package org.kkeunkkeun.pregen.practice.presentation

import org.kkeunkkeun.pregen.practice.service.MessageHandler
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class PracticeWebSocketController(
    private val messageHandler: MessageHandler,
    val messageTemplate: SimpMessagingTemplate,
) {

    @MessageMapping("/practice/{sessionId}")
    fun enter(
        @DestinationVariable sessionId: String,
        @Payload request: BaseMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): Message {
        val handle = messageHandler.handle(request)
        messageTemplate.convertAndSend("/sub/practice/$sessionId", handle)
        return handle
    }

    @MessageMapping("/practice/{sessionId}/ping")
    fun healthCheck(
        @DestinationVariable sessionId: String,
        headerAccessor: SimpMessageHeaderAccessor,
    ): Message {
        val baseMessage = BaseMessage(sessionId, "HEALTH_CHECK", "Pong!")
        messageTemplate.convertAndSend("/sub/practice/$sessionId", baseMessage)
        return baseMessage
    }
}
