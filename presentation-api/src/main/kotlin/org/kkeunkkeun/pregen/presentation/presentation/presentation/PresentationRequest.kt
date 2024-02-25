package org.kkeunkkeun.pregen.presentation.presentation.presentation

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

data class PresentationRequest(
    @field:Length(min = 0, max = 255)
    val title: String,

    @field:FutureOrPresent
    val deadlineDate: LocalDate,

    @field:NotNull
    val timeLimit: PresentationTime,

    val alertTime: PresentationTime? = null,

    @field:Size(min = 1, max = 30)
    val slides: List<SlideRequest>,
) {

    data class SlideRequest(
        val imageFileId: Long? = null,

        @field:Length(min = 0, max = 5000)
        val script: String = "",

        @field:Length(min = 0, max = 500)
        val memo: String = "",
    )

    data class Delete(
        @field:NotNull
        val presentationIds: List<Long>
    )
}