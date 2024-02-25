package org.kkeunkkeun.pregen.presentation.practice.presentation

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence

data class PracticeRequest(
    @field:NotNull
    val practiceMode: PracticeMode,

    val slides: List<SlideRequest>? = listOf()
) {

    data class SlideRequest(
        @field:NotNull
        val id: Long,

        val memorizationSentences: List<MemorizationSentenceRequest>? = listOf(),
    )

    data class MemorizationSentenceRequest(
        @field:Min(0)
        val offset: Int,

        @field:Min(0)
        val length: Int
    ) {

        fun to(slideId: Long): MemorizationSentence {
            return MemorizationSentence(
                slideId = slideId,
                startIndex = this.offset,
                endIndex = this.offset + this.length
            )
        }
    }
}
