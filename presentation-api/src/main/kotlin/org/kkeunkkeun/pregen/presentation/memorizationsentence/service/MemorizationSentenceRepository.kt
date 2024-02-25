package org.kkeunkkeun.pregen.presentation.memorizationsentence.service

import org.kkeunkkeun.pregen.presentation.memorizationsentence.domain.MemorizationSentence

interface MemorizationSentenceRepository {

    fun findBySlideId(slideId: Long): List<MemorizationSentence>

    fun saveAll(memorizationSentences: List<MemorizationSentence>)
}