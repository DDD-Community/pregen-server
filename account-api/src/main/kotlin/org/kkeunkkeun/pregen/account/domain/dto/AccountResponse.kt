package org.kkeunkkeun.pregen.account.domain.dto

data class AccountResponse(
    val email: String,
    val nickName: String,
    val socialProvider: String,
)