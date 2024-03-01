package org.kkeunkkeun.pregen.presentation.practice.infrastructure

import org.kkeunkkeun.pregen.presentation.practice.domain.Practice
import org.kkeunkkeun.pregen.presentation.practice.service.PracticeRepository
import org.springframework.stereotype.Repository

@Repository
class PracticeRepositoryImpl(
    private val practiceJpaRepository: PracticeJpaRepository,
    private val practiceQueryRepository: PracticeQueryRepository,
): PracticeRepository {

    override fun findLatestByPresentationId(presentationId: Long): Practice {
        return practiceJpaRepository.findFirstByPresentationIdOrderByIdDesc(presentationId)
            ?: throw RuntimeException() // TODO
    }

    override fun findLatestByAccountId(accountId: Long): Practice? {
        return practiceQueryRepository.findLatestByAccountId(accountId)
    }

    override fun save(practice: Practice) {
        practiceJpaRepository.save(practice)
    }
}