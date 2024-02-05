package org.kkeunkkeun.pregen.presentation.presentation.infrastructure

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.domain.PresentationStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PresentationJpaRepository : JpaRepository<Presentation, Long> {

    fun findByAccountIdAndStatus(accountId: Long, status: PresentationStatus, pageable: Pageable): Page<Presentation>
}