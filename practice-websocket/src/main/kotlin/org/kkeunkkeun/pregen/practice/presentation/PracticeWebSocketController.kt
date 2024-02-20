package org.kkeunkkeun.pregen.practice.presentation

import org.kkeunkkeun.pregen.practice.service.MessageHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class PracticeWebSocketController(
    private val messageHandler: MessageHandler,
) {

    @MessageMapping("/practice/{sessionId}")
    @SendTo("/sub/practice/{sessionId}")
    fun enter(
        @Payload request: BaseMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): BaseMessage {
        return messageHandler.handle(request)
    }

    @MessageMapping("/practice/{sessionId}/ping")
    @SendTo("/sub/practice/{sessionId}")
    fun healthCheck(
        @Payload request: BaseMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): BaseMessage {
        return BaseMessage(request.sessionId, "HEALTH_CHECK", "Pong!")
    }
}
