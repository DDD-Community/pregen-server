package org.kkeunkkeun.pregen.presentation.practice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Practice(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "practice_id")
    val id: Long? = null,

    val presentationId: Long,

    val title: String,

    val timeLimit: Int,

    val practiceTime: Int
)
