package org.kkeunkkeun.pregen.practice.presentation

data class InsertMessage(
    override val sessionId: String,
    override val message: String,
    val notificationStatus: String,
    val slideIndex: String,
    val accumulatedPresentationTime: String,
    val recordCondition: String
): Message
