package org.kkeunkkeun.pregen.common.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND

enum class ErrorStatus(
    val errorCode: Int,
    val message: String?,
    val status: HttpStatus,
) {

    /* 404 */
    DATA_NOT_FOUND(40401, "리소스를 찾을 수 없습니다.", NOT_FOUND),
    HASH_FIELD_NOT_FOUND(40402, "해당 필드를 찾을 수 없습니다.", NOT_FOUND),

    /* 400 */
    BAD_REQUEST(40001, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

    /* 401 */
    UNAUTHORIZED(40101, "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(40102, "인증되지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
}
