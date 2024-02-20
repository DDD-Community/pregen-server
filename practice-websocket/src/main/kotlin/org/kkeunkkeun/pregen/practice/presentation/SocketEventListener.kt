package org.kkeunkkeun.pregen.practice.presentation

import org.kkeunkkeun.pregen.account.service.AccountService
import org.kkeunkkeun.pregen.practice.service.PracticeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class SocketEventListener (
    private val practiceService: PracticeService,
){

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventListener::class.java)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor: SimpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(event.message)
        practiceService.deletePractice(headerAccessor.sessionId!!)
        logger.info("Web socket ${headerAccessor.sessionId} session closed. Message : [{}]", headerAccessor.sessionId, event.message)
    }
}
