package org.kkeunkkeun.pregen.practice.service

import org.kkeunkkeun.pregen.practice.presentation.BaseMessage
import org.kkeunkkeun.pregen.practice.presentation.Message

interface MessageHandler {
    fun handle(message: BaseMessage): Message
}