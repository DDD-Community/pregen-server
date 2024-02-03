package org.kkeunkkeun.pregen.common.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND

enum class ErrorStatus(
    val errorCode: Int,
    val message: String?,
    val status: HttpStatus,
) {

    /* 404 */
    DATA_NOT_FOUND(40401, "리소스를 찾을 수 없습니다.", NOT_FOUND)
}
