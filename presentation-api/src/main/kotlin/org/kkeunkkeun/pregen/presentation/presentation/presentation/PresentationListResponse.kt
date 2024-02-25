package org.kkeunkkeun.pregen.presentation.presentation.presentation

import org.kkeunkkeun.pregen.common.presentation.CustomPage
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.springframework.data.domain.Page
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*

data class PresentationListResponse(
    val page: CustomPage<ListItem>,
) {

    data class ListItem(

        val id: Long,

        val title: String,

        val dDay: Int,

        val timeLimit: PresentationTime,

        val thumbnailPath: String?,

        val createdAt: LocalDateTime,

        val modifiedAt: LocalDateTime,
    ) {

        companion object {
            fun from(presentation: Presentation, thumbnailPath: String?): ListItem {
                val id = presentation.id ?: throw RuntimeException()
                val createdAt = presentation.createdAt
                val modifiedAt = presentation.modifiedAt ?: throw RuntimeException()

                return ListItem(
                    id,
                    presentation.title,
                    presentation.getDDay(),
                    PresentationTime.from(presentation.timeLimit),
                    thumbnailPath,
                    createdAt,
                    modifiedAt,
                )
            }
        }
    }
}