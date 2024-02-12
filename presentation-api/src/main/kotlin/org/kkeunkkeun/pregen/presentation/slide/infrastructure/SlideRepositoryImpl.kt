package org.kkeunkkeun.pregen.presentation.slide.infrastructure

import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.service.SlideRepository
import org.springframework.stereotype.Repository

@Repository
class SlideRepositoryImpl(
    private val slideJpaRepository: SlideJpaRepository
): SlideRepository {

    override fun findByPracticeId(practiceId: Long): List<Slide> {
        return slideJpaRepository.findByPracticeId(practiceId)
    }

    override fun saveAll(slides: List<Slide>) {
        slideJpaRepository.saveAll(slides)
    }
}