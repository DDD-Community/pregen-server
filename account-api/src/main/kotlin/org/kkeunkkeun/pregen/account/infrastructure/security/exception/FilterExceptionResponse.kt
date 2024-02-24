package org.kkeunkkeun.pregen.account.infrastructure.security.exception

import org.kkeunkkeun.pregen.common.presentation.ErrorResponse
import java.time.LocalDateTime

class FilterExceptionResponse(
    val timestamp: LocalDateTime,
    val error: String,
    val code: Int,
    val message: String?,
    val path: String,
) {
    companion object {

        fun from(filterException: FilterException, path: String): ErrorResponse {
            val timestamp = LocalDateTime.now()
            val errorStatus = filterException.errorStatus

            return ErrorResponse(timestamp, errorStatus.name, errorStatus.errorCode, filterException.message, path)
        }
    }
}