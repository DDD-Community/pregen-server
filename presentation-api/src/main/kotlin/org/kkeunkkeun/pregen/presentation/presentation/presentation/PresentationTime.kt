package org.kkeunkkeun.pregen.presentation.presentation.presentation

import jakarta.validation.constraints.Min

data class PresentationTime(

    @field:Min(0)
    val hours: Int,

    @field:Min(1)
    val minutes: Int,
) {

    fun toMinutes(): Int {
        return hours * 60 + minutes
    }

    companion object {
        fun from(totalMinutes: Int): PresentationTime {
            return PresentationTime(
                hours = totalMinutes / 60,
                minutes = totalMinutes % 60,
            )
        }
    }
}
