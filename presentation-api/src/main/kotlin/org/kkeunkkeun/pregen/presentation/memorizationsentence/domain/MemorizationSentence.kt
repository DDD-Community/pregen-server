package org.kkeunkkeun.pregen.presentation.memorizationsentence.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import org.kkeunkkeun.pregen.presentation.practice.presentation.PracticeMode

@Entity
class MemorizationSentence(

    slideId: Long?,

    val startIndex: Int,

    val endIndex: Int,
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memorization_sentence_id")
    val id: Long? = null

    var slideId: Long? = slideId
        protected set

    fun unmap() {
        this.slideId = null
    }
}

