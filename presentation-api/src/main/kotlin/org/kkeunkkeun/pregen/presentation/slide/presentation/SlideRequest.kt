package org.kkeunkkeun.pregen.presentation.slide.presentation

import org.hibernate.validator.constraints.Length

data class SlideRequest(

    @field:Length(min = 0, max = 500)
    val memo: String = "",

    val audioFileId: Long? = null,
)
