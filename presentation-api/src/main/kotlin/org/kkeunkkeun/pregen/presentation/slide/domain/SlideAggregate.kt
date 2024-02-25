package org.kkeunkkeun.pregen.presentation.slide.domain

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence


data class SlideAggregate(
    val slide: Slide,
    val memorizationSentences: List<MemorizationSentence>
)
