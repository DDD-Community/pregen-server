package org.kkeunkkeun.pregen.account.infrastructure.security.exception

import org.kkeunkkeun.pregen.common.presentation.ErrorStatus

class FilterException(
    val errorStatus: ErrorStatus,
    override val message: String,
): RuntimeException()