package org.kkeunkkeun.pregen.presentation.presentation.presentation

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import java.lang.RuntimeException
import java.time.LocalDateTime

class PresentationResponse {

    data class PresentationPersist(
        val presentationId: Long,
    )

    data class PresentationDetail(

        val id: Long,

        val title: String,

        val dDay: Int,

        val timeLimit: Int,

        val alertBeforeLimit: Boolean,

        val createdAt: LocalDateTime,

        val modifiedAt: LocalDateTime,

        val slides: List<SlideDetail>,
    ) {
        companion object {
            fun from(presentation: Presentation, slides: List<Slide>): PresentationDetail {
                val id = presentation.id ?: throw RuntimeException()
                val createdAt = presentation.createdAt ?: throw RuntimeException()
                val modifiedAt = presentation.modifiedAt ?: throw RuntimeException()

                return PresentationDetail(
                    id,
                    presentation.title,
                    presentation.getDDay(),
                    presentation.timeLimit,
                    presentation.alertBeforeLimit,
                    createdAt,
                    modifiedAt,
                    SlideDetail.from(slides),
                )
            }
        }
    }

    data class SlideDetail(

        val id: Long,

        val imageFilePath: String?,

        val script: String,

        val memo: String,
    ) {
        companion object {
            fun from(slides: List<Slide>): List<SlideDetail> {
                return slides.map { slide -> SlideDetail(slide.id!!, slide.imageFilePath(), slide.script, slide.memo) }
            }
        }
    }

}