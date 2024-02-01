package org.kkeunkkeun.pregen.presentation.presentation.service

import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationListResponse
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class PresentationService(
    private val presentationRepository: PresentationRepository
) {

    fun findListByMemberId(memberId: Long, pageable: Pageable): Page<PresentationListResponse.ListItem> {
        return presentationRepository.findByMemberId(memberId, pageable) // TODO: memberId 추가
            .map { presentation -> generateListItem(presentation) }
    }

    fun generateListItem(presentation: Presentation): PresentationListResponse.ListItem {
        return PresentationListResponse.ListItem.from(presentation)
    }

//    fun findById(id: Long): Presentation {
//        return presentationRepository.findByIdOrNull(id)
//            ?: throw RuntimeException()
//    }

    fun save(memberId: Long, request: PresentationRequest): Long {
        val presentation = Presentation.from(memberId, request)

        presentationRepository.save(presentation)

        return presentation.id ?: throw RuntimeException() // TODO
    }
}