package org.kkeunkkeun.pregen.practice.service

import org.kkeunkkeun.pregen.practice.presentation.BaseMessage

interface MessageHandler {
    fun handle(message: BaseMessage): BaseMessage
}