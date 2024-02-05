package org.kkeunkkeun.pregen.account.presentation.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * 특정 파라미터에 계정의 email 값을 주입해주기 위한 용도의 어노테이션
 *
 * @see org.kkeunkkeun.pregen.account.presentation.annotation
 */
@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class AccountEmail()
