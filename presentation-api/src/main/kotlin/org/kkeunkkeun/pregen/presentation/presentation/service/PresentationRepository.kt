package org.kkeunkkeun.pregen.presentation.presentation.service

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PresentationRepository {

    fun findByAccountId(accountId: Long, pageable: Pageable): Page<Presentation>
    fun findByIdOrNull(id: Long): Presentation?
    fun save(presentation: Presentation)
}