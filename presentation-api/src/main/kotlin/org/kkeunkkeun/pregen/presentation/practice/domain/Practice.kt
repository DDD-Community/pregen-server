package org.kkeunkkeun.pregen.presentation.practice.domain

import jakarta.persistence.*
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import org.kkeunkkeun.pregen.presentation.practice.presentation.PracticeMode

@Entity
class Practice(

    val presentationId: Long,

    val title: String,

    val timeLimit: Int,

    practiceTime: Int? = null,

    mode: PracticeMode,
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "practice_id")
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    var mode: PracticeMode = mode
        protected set

    var practiceTime: Int? = practiceTime
        protected set

    fun updateMode(mode: PracticeMode) {
        this.mode = mode
    }

    fun done(practiceTime: Int) {
        this.practiceTime = practiceTime
    }
}
