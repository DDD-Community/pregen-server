package org.kkeunkkeun.pregen.presentation.presentation.infrastructure

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.domain.PresentationStatus.NORMAL
import org.kkeunkkeun.pregen.presentation.presentation.service.PresentationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PresentationRepositoryImpl(
    private val presentationJpaRepository: PresentationJpaRepository
): PresentationRepository {

    override fun findByAccountId(accountId: Long, pageable: Pageable): Page<Presentation> {
        return presentationJpaRepository.findByAccountIdAndStatus(accountId, NORMAL, pageable)
    }

    override fun findByIdOrNull(id: Long): Presentation? {
        return presentationJpaRepository.findByIdOrNull(id)
    }

    override fun save(presentation: Presentation) {
        presentationJpaRepository.save(presentation)
    }
}