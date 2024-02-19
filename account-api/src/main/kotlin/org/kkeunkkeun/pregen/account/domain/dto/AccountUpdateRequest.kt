package org.kkeunkkeun.pregen.account.domain.dto

import jakarta.validation.constraints.NotBlank

data class AccountUpdateRequest(
    @field:NotBlank(message = "닉네임을 입력해주세요.")
    val nickName: String,
)
