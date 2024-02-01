package org.kkeunkkeun.pregen.account.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class AccountUpdateRequest(
    @field:NotBlank(message = "닉네임을 입력해주세요.")
    val nickName: String,
) {
}