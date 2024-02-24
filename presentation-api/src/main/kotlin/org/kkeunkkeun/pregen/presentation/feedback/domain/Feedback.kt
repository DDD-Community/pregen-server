package org.kkeunkkeun.pregen.presentation.feedback.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Feedback(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    val id: Long? = null,

    val practiceId: Long,

    @Column(columnDefinition = "smallint unsigned")
    val score: Int
)
