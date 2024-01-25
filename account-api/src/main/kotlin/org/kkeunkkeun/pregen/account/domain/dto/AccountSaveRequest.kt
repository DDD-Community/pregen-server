package org.kkeunkkeun.pregen.account.domain.dto

import org.springframework.web.multipart.MultipartFile

data class AccountSaveRequest(
    val email: String,
    var nickName: String?,
    val socialType: String,

    val profileImg: MultipartFile?,
)
