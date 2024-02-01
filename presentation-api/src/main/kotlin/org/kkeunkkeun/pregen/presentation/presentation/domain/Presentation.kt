package org.kkeunkkeun.pregen.presentation.presentation.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.kkeunkkeun.pregen.common.domain.BaseTimeEntity
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity
class Presentation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presentation_id")
    val id: Long? = null,

    val memberId: Long,

    val title: String,

    val deadlineDate: LocalDate,

    val includeDeadlineDate: Boolean,

    val timeLimit: Int,

    val alertBeforeLimit: Boolean,

    @Enumerated(STRING)
    @Column(length = 30)
    val status: PresentationStatus

): BaseTimeEntity() {

    companion object {
        fun from(memberId: Long, presentationRequest: PresentationRequest): Presentation {
            return Presentation(null, memberId, presentationRequest.title, presentationRequest.deadlineDate,
                presentationRequest.includeDeadlineDate, presentationRequest.timeLimit, presentationRequest.alertBeforeLimit,
                PresentationStatus.NORMAL)
        }
    }

    fun getDDay(): Int {
        val today = LocalDate.now()
        var dDay = ChronoUnit.DAYS.between(today, deadlineDate)

        if (includeDeadlineDate) {
            dDay += 1
        }

        return dDay.toInt()
    }

    fun getTimeLimitAsMinute(): Int {
        return timeLimit / 60
    }
}