package org.kkeunkkeun.pregen.presentation.memorizationsentence.infrastructure

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence
import org.springframework.data.jpa.repository.JpaRepository

interface MemorizationSentenceJpaRepository: JpaRepository<MemorizationSentence, Long> {

    fun findBySlideId(slideId: Long): List<MemorizationSentence>
}