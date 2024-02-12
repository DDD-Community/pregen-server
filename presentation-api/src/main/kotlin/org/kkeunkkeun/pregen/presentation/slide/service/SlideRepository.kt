package org.kkeunkkeun.pregen.presentation.slide.service

import org.kkeunkkeun.pregen.presentation.slide.domain.Slide

interface SlideRepository {

    fun findByPracticeId(practiceId: Long): List<Slide>

    fun saveAll(slides: List<Slide>)
}