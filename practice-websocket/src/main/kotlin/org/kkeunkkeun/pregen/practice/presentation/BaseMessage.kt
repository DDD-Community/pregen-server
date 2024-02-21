package org.kkeunkkeun.pregen.practice.presentation

data class BaseMessage(
    override val sessionId: String,
    override val message: String,
    val key: String? = null,
    val value: String? = null
): Message
