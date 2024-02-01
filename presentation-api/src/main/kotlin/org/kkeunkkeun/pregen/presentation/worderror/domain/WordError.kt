package org.kkeunkkeun.pregen.presentation.worderror.domain

import jakarta.persistence.*

class WordError(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_error_id")
    val id: Long? = null,

    val slideId: Long,

    val startIndex: Int,

    val endIndex: Int
)
