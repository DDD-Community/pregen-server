package org.kkeunkkeun.pregen.presentation.slide.infrastructure

import org.kkeunkkeun.pregen.presentation.slide.domain.Slide
import org.kkeunkkeun.pregen.presentation.slide.domain.SlideAggregate
import org.kkeunkkeun.pregen.presentation.slide.service.SlideRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class SlideRepositoryImpl(
    private val slideJpaRepository: SlideJpaRepository,
    private val slideQueryRepository: SlideQueryRepository,
): SlideRepository {

    override fun findByPracticeId(practiceId: Long): List<Slide> {
        return slideJpaRepository.findByPracticeId(practiceId)
    }

    override fun findFirstByPracticeId(practiceId: Long): Slide? {
        return slideJpaRepository.findFirstByPracticeId(practiceId)
    }

    override fun saveAll(slides: List<Slide>) {
        slideJpaRepository.saveAll(slides)
    }

    override fun findAggregatesByPracticeId(practiceId: Long): List<SlideAggregate> {
        return this.findByPracticeId(practiceId)
            .map { slide -> slideQueryRepository.findAggregateBySlideId(slide.id!!) }
    }

    override fun findById(slideId: Long): Slide? {
        return slideJpaRepository.findByIdOrNull(slideId)
    }
}
