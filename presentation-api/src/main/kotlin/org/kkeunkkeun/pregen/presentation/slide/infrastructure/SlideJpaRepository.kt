package org.kkeunkkeun.pregen.presentation.slide.infrastructure

import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SlideJpaRepository: JpaRepository<Slide, Long> {

    fun findByPracticeId(practiceId: Long): List<Slide>

    fun findFirstByPracticeId(practiceId: Long): Slide?
}
