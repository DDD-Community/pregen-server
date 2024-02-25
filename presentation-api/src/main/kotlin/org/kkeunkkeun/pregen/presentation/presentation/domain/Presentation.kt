package org.kkeunkkeun.pregen.presentation.presentation.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import org.kkeunkkeun.pregen.presentation.presentation.domain.PresentationStatus.DELETED
import org.kkeunkkeun.pregen.presentation.presentation.domain.PresentationStatus.NORMAL
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationTime
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity
class Presentation(

    val accountId: Long,

    title: String,

    deadlineDate: LocalDate,

    timeLimit: Int,

    alertTime: Int? = null,
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presentation_id")
    val id: Long? = null

    var title: String = title
        protected set

    var deadlineDate: LocalDate = deadlineDate
        protected set

    var timeLimit: Int = timeLimit
        protected set

    var alertTime: Int? = alertTime
        protected set

    @Enumerated(STRING)
    @Column(length = 30)
    var status: PresentationStatus = NORMAL
        protected set

    companion object {
        fun from(accountId: Long, presentationRequest: PresentationRequest): Presentation {
            return Presentation(accountId, presentationRequest.title, presentationRequest.deadlineDate,
                presentationRequest.timeLimit.toMinutes(), presentationRequest.alertTime?.toMinutes())
        }
    }

    fun getDDay(): Int {
        val today = LocalDate.now()
        val dDay = ChronoUnit.DAYS.between(today, deadlineDate)

        return dDay.toInt()
    }

    fun isNotOwnerOfPresentation(accountId: Long): Boolean {
        return accountId != this.accountId
    }

    fun checkDeleted() {
        if (this.status == DELETED) {
            throw IllegalStateException("This presentation is deleted.")
        }
    }

    fun update(title: String, deadlineDate: LocalDate, timeLimit: PresentationTime, alertTime: PresentationTime?) {
        this.title = title
        this.deadlineDate = deadlineDate
        this.timeLimit = timeLimit.toMinutes()
        this.alertTime = alertTime?.toMinutes()
    }

    fun delete() {
        if (this.status == DELETED) {
            throw IllegalStateException(String.format("Presentation id = %d that has already been deleted.", this.id))
        }
        this.status = DELETED
    }
}