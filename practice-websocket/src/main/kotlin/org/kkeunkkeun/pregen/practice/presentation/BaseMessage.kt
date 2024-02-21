package org.kkeunkkeun.pregen.practice.presentation

data class BaseMessage(
    val sessionId: String,
    val message: String,
    val key: String? = null,
    val value: String? = null
)
