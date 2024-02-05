package org.kkeunkkeun.pregen.common.presentation

import org.springframework.http.ResponseEntity

class PregenException(
    val errorStatus: ErrorStatus,
    message: String? = null,
): RuntimeException(message) {

    fun toResponseEntity(path: String): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(this.errorStatus.status)
            .body(ErrorResponse.from(this, path))
    }

    constructor(errorStatus: ErrorStatus): this(errorStatus, errorStatus.message)
}
