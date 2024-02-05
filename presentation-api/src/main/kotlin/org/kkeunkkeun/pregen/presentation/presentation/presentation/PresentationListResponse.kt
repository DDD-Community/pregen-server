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

        val timeLimitAsMinute: Int,

        val createdAt: LocalDateTime,
    ) {

        companion object {
            fun from(presentation: Presentation): ListItem {
                val id = presentation.id ?: throw RuntimeException()
                val createdAt = presentation.createdAt ?: throw RuntimeException()

                return ListItem(
                    id,
                    presentation.title,
                    presentation.getDDay(),
                    presentation.getTimeLimitAsMinute(),
                    createdAt
                )
            }
        }
    }
}