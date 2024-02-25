package org.kkeunkkeun.pregen.presentation.slide.service

import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate

interface SlideRepository {

    fun findByPracticeId(practiceId: Long): List<Slide>

    fun findFirstByPracticeId(practiceId: Long): Slide?

    fun saveAll(slides: List<Slide>)

    fun findAggregatesByPracticeId(practiceId: Long): List<SlideAggregate>

    fun findById(slideId: Long): Slide?
}