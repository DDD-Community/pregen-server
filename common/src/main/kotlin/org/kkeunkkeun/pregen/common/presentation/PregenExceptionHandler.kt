package org.kkeunkkeun.pregen.common.presentation

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PregenExceptionHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @ExceptionHandler(PregenException::class)
    fun handlePregenException(request: HttpServletRequest, exception: PregenException): ResponseEntity<ErrorResponse> {
        log.error("pregen exception occurred!!", exception)

        return exception.toResponseEntity(request.requestURI)
    }
}
