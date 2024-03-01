package org.kkeunkkeun.pregen.practice.service

import org.kkeunkkeun.pregen.practice.presentation.BaseMessage
import org.kkeunkkeun.pregen.practice.presentation.InsertMessage
import org.kkeunkkeun.pregen.practice.presentation.Message
import org.springframework.stereotype.Service

@Service
class MessageHandlerImpl(
    private val practiceService: PracticeService,
): MessageHandler {

    override fun handle(message: BaseMessage): Message {
        return when (message.message) {
            "INSERT" -> handleInsert(message.sessionId, message)
            "UPDATE" -> handleUpdate(message.sessionId, message)
            "GET" -> handleGet(message.sessionId, message)
            else -> BaseMessage(message.sessionId, "ERROR")
        }
    }

    private fun handleInsert(sessionId: String, message: BaseMessage): InsertMessage {
        val notificationStatus = message.key.toBoolean()
        return practiceService.insertPractice(sessionId, notificationStatus)
    }

    private fun handleUpdate(sessionId: String, message: BaseMessage): BaseMessage {
        practiceService.updatePractice(sessionId, message.key!!, message.value!!)
        return BaseMessage(sessionId, "UPDATE", message.key, message.value)
    }

    private fun handleGet(sessionId: String, message: BaseMessage): BaseMessage {
        val value = practiceService.getPractice(sessionId, message.key!!)
        return BaseMessage(sessionId, "GET", message.key, value)
    }
}
