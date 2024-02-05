package org.kkeunkkeun.pregen.presentation.practice.infrastructure

import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.springframework.data.jpa.repository.JpaRepository

interface PracticeJpaRepository : JpaRepository<Practice, Long> {

    fun findFirstByPresentationIdOrderByIdDesc(presentationId: Long): Practice?
}