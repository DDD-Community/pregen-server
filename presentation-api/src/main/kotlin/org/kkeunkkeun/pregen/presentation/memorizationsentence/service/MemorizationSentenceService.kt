package org.kkeunkkeun.pregen.presentation.memorizationsentence.service

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence
import org.kkeunkkeun.pregen.presentation.practice.presentation.PracticeRequest.MemorizationSentenceRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemorizationSentenceService(
    private val memorizationSentenceRepository: MemorizationSentenceRepository,
) {

    fun overwrite(slideId: Long, requests: List<MemorizationSentenceRequest>?) {
        memorizationSentenceRepository.findBySlideId(slideId)
            .forEach(MemorizationSentence::unmap)


        requests?.let {
            memorizationSentenceRepository.saveAll(
                it.map { request -> request.to(slideId) }
            )
        }
    }
}