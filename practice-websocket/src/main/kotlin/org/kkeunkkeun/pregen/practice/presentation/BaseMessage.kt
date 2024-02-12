package org.kkeunkkeun.pregen.practice.presentation

data class BaseMessage(
    val sessionId: String,
    val type: String,
    val message: String,
)
