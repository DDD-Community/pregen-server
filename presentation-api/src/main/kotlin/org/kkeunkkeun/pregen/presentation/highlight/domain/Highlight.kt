package org.kkeunkkeun.pregen.presentation.highlight.domain

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Highlight(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    val id: Long? = null,

    val slideId: Long,

    val startIndex: Int,

    val endIndex: Int
)
