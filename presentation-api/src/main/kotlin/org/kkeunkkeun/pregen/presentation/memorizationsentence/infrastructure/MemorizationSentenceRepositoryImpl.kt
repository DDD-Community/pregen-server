package org.kkeunkkeun.pregen.presentation.memorizationsentence.infrastructure

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence
import org.kkeunkkeun.pregen.presentation.memorizationsentence.service.MemorizationSentenceRepository
import org.springframework.stereotype.Repository

@Repository
class MemorizationSentenceRepositoryImpl(
    private val memorizationSentenceJpaRepository: MemorizationSentenceJpaRepository,
): MemorizationSentenceRepository {

    override fun findBySlideId(slideId: Long): List<MemorizationSentence> {
        return memorizationSentenceJpaRepository.findBySlideId(slideId)
    }

    override fun saveAll(memorizationSentences: List<MemorizationSentence>) {
        memorizationSentenceJpaRepository.saveAll(memorizationSentences)
    }
}