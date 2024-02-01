package org.kkeunkkeun.pregen.presentation.presentation.presentation

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class PresentationRequest(
    @field:Size(min = 0, max = 255)
    val title: String,

    @field:FutureOrPresent
    val deadlineDate: LocalDate,

    @field:NotNull
    val includeDeadlineDate: Boolean,

    @field:Min(0)
    val timeLimit: Int,

    @field:NotNull
    val alertBeforeLimit: Boolean,

    @field:NotNull
    val slides: List<SlideRequest>,
) {

    data class SlideRequest(
        val imageFileId: Long? = null,

        @field:Size(min = 0, max = 50000)
        val script: String,
    )
}