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

    @field:Min(1)
    val timeLimit: Int,

    @field:NotNull
    val alertBeforeLimit: Boolean,

    @field:Size(min = 1)
    val slides: List<SlideRequest>,
) {

    data class SlideRequest(
        val imageFileId: Long? = null,

        @field:Size(min = 0, max = 5000)
        val script: String = "",

        @field:Size(min = 0, max = 500)
        val memo: String = "",
    )

    data class Delete(
        @field:NotNull
        val presentationIds: List<Long>
    )
}