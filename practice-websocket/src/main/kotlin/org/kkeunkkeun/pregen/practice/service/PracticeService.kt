package org.kkeunkkeun.pregen.practice.service

import org.kkeunkkeun.pregen.account.service.AccountService
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.kkeunkkeun.pregen.practice.infrastructure.SocketProperties
import org.kkeunkkeun.pregen.practice.presentation.InsertMessage
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class PracticeService(
    private val redisService: RedisService,
    private val socketProperties: SocketProperties,
    private val accountService: AccountService,
) {

    fun insertPractice(sessionId: String, notificationStatus: Boolean): InsertMessage {
        redisService.updateHashField(sessionId, socketProperties.notificationStatus, notificationStatus.toString())
        val slideIndex = redisService.updateHashField(sessionId, socketProperties.slideIndex, "1")
        val accumulatedPresentationTime = redisService.updateHashField(sessionId, socketProperties.accumulatedPresentationTime, null.toString())
        val recordCondition = redisService.updateHashField(sessionId, socketProperties.recordCondition, "true")
        startRecording(sessionId)

        return InsertMessage(
            sessionId, "INSERT",
            notificationStatus.toString(), slideIndex, accumulatedPresentationTime, recordCondition
        )
    }

    fun updatePractice(sessionId: String, key: String, value: String) {
        when (key) {
            socketProperties.notificationStatus -> {
                validateAndUpdateField(sessionId, socketProperties.notificationStatus, value)
            }
            socketProperties.slideIndex -> {
                redisService.updateHashField(sessionId, socketProperties.slideIndex, value)
            }
            socketProperties.recordCondition -> {
                validateAndUpdateField(sessionId, socketProperties.recordCondition, value)
            }
        }
    }

    fun deletePractice(sessionId: String) {
        redisService.deleteAllFields(sessionId)
        accountService.updateAccountSessionId(sessionId)
    }

    fun getPractice(sessionId: String, field: String): String? {
        // 누적 시간 필드일 경우 누적 시간 계산후에 값을 가져옴
        if (field == socketProperties.accumulatedPresentationTime) {
            return calculateAccumulatedPresentationTime(sessionId)
        }
        return redisService.getHashField(sessionId, field)
    }

    private fun validateAndUpdateField(sessionId: String, field: String, value: String) {
        when (field) {
            socketProperties.recordCondition -> {
                when (value) {
                    "true" -> startRecording(sessionId)
                    "false" -> stopRecording(sessionId, field, value)
                    else -> throw PregenException(ErrorStatus.BAD_REQUEST)
                }
            }
            socketProperties.notificationStatus -> {
                when (value) {
                    "true", "false" -> redisService.updateHashField(sessionId, field, value)
                    else -> throw PregenException(ErrorStatus.BAD_REQUEST)
                }
            }
        }
    }

    private fun startRecording(sessionId: String) {
        redisService.updateHashField(sessionId, socketProperties.recentPresentationStartTime, Instant.now().toString())
    }

    private fun stopRecording(sessionId: String, field: String, value: String) {
        calculateAccumulatedPresentationTime(sessionId)
        redisService.updateHashField(sessionId, field, value)
    }

    private fun calculateAccumulatedPresentationTime(sessionId: String): String {
        // 누적 시간 계산을 위한 변수
        var newAccumulatedTime: Duration = Duration.ZERO

        val startTimeString = redisService.getHashField(sessionId, socketProperties.recentPresentationStartTime)
        if (startTimeString != null) { // null 체크를 if로 변경
            val startTime = Instant.parse(startTimeString)
            val stopTime = Instant.now()
            val duration = Duration.between(startTime, stopTime)

            // 기존에 저장된 누적 시간 가져오기
            val accumulatedTimeString = redisService.getHashField(sessionId, socketProperties.accumulatedPresentationTime)
            val accumulatedTime = accumulatedTimeString?.let { Duration.parse(it) } ?: Duration.ZERO

            // 새로운 누적 시간 계산
            newAccumulatedTime = accumulatedTime.plus(duration)

            // 누적 시간 업데이트
            redisService.updateHashField(sessionId, socketProperties.accumulatedPresentationTime, newAccumulatedTime.toString())
        }
        // startTimeString이 null인 경우, 이미 초기화된 Duration.ZERO (또는 이전 값)을 반환합니다.
        return newAccumulatedTime.toString() // 누적 시간 반환
    }
}
