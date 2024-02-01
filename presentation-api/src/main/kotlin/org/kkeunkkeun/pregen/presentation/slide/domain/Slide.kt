package org.kkeunkkeun.pregen.presentation.slide.domain

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Slide(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slide_id")
    val id: Long? = null,

    val practiceId: Long,

    val imageFileId: Long? = null,

    @Column(columnDefinition = "text")
    val script: String
)
