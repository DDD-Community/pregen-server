package org.kkeunkkeun.pregen.practice.presentation

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class PracticeWebSocketController {

    @MessageMapping("/practice/{sessionId}")
    @SendTo("/sub/practice/{sessionId}")
    fun enter(
        @Payload request: BaseMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): BaseMessage {
        return request
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