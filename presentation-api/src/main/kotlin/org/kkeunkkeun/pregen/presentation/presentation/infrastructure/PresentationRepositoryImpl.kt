package org.kkeunkkeun.pregen.presentation.presentation.infrastructure

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PresentationRepositoryImpl(
    private val presentationJpaRepository: PresentationJpaRepository
): PresentationRepository {

    override fun findByMemberId(memberId: Long, pageable: Pageable): Page<Presentation> {
        return presentationJpaRepository.findByMemberId(memberId, pageable)
    }

    override fun save(presentation: Presentation) {
        presentationJpaRepository.save(presentation)
    }
}