package org.kkeunkkeun.pregen.presentation.practice.service

import org.kkeunkkeun.pregen.presentation.practice.domain.Practice

interface PracticeRepository {

    fun findLatestByPresentationId(presentationId: Long): Practice

    fun save(practice: Practice)
}