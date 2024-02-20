package org.kkeunkkeun.pregen.practice.service

import org.kkeunkkeun.pregen.practice.presentation.BaseMessage

class MessageHandlerImpl(
    private val practiceService: PracticeService,
): MessageHandler {

    override fun handle(message: BaseMessage): BaseMessage {
        return when (message.command) {
            "INSERT" -> handleInsert(message.sessionId, message)
            "UPDATE" -> handleUpdate(message.sessionId, message)
            "GET" -> handleGet(message.sessionId, message)
            else -> BaseMessage(message.sessionId, "ERROR", "Invalid action")
        }
    }

    private fun handleInsert(sessionId: String, message: BaseMessage): BaseMessage {
        val notificationStatus = message.key.toBoolean()
        practiceService.insertPractice(sessionId, notificationStatus)
        return BaseMessage(sessionId, "INSERT", "Presentation inserted")
    }

    private fun handleUpdate(sessionId: String, message: BaseMessage): BaseMessage {
        practiceService.updatePractice(sessionId, message.key!!, message.value!!)
        return BaseMessage(sessionId, "UPDATE", "Presentation updated", message.key, message.value)
    }

    private fun handleGet(sessionId: String, message: BaseMessage): BaseMessage {
        val value = practiceService.getPractice(sessionId, message.key!!)
        return BaseMessage(sessionId, "GET", value ?: "No such field")
    }
}
