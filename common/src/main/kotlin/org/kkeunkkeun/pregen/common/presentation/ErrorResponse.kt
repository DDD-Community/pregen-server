package org.kkeunkkeun.pregen.common.presentation

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime


data class ErrorResponse(
    val timestamp: LocalDateTime,
    val error: String,
    val code: Int,
    val message: String?,
    val path: String,
) {

    companion object {

        fun from(pregenException: PregenException, path: String): ErrorResponse {
            val timestamp = LocalDateTime.now()
            val errorStatus = pregenException.errorStatus

            return ErrorResponse(timestamp, errorStatus.name, errorStatus.errorCode, pregenException.message, path)
        }
    }
}
