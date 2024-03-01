package org.kkeunkkeun.pregen.presentation.presentation.service

import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationListResponse
import org.kkeunkkeun.pregen.presentation.presentation.presentation.PresentationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PresentationService(
    private val presentationRepository: PresentationRepository,
    private val accountRepository: AccountRepository,
    private val presentationAccessChecker: PresentationAccessChecker,
    private val thumbnailResolver: PresentationThumbnailResolver
) {

    fun findListByAccountId(email: String, pageable: Pageable): Page<PresentationListResponse.ListItem> {
        val accountId = accountRepository.findIdByEmail(email)
            ?: throw RuntimeException()

        return presentationRepository.findByAccountId(accountId, pageable)
            .map { presentation -> generateListItem(presentation) }
    }

    fun findById(email: String, id: Long): Presentation {
        val presentation = presentationRepository.findByIdOrNull(id)
            ?: throw RuntimeException()
        presentationAccessChecker.checkAccess(presentation, email)

        return presentation
    }

    @Transactional
    fun save(email: String, request: PresentationRequest): Presentation {
        val accountId = accountRepository.findIdByEmail(email)
            ?: throw RuntimeException()
        val presentation = Presentation.from(accountId, request)

        presentationRepository.save(presentation)

        return presentation
    }

    @Transactional
    fun update(email: String, presentationId: Long, request: PresentationRequest): Long {
        val presentation = this.findById(email, presentationId)
        presentation.update(request.title, request.deadlineDate, request.timeLimit, request.alertTime)

        return presentation.id!!
    }

    @Transactional
    fun deleteByIds(email: String, presentationIds: List<Long>) {
        presentationIds.forEach { id -> this.deleteById(email, id) }
    }

    private fun deleteById(email: String, presentationId: Long) {
        this.findById(email, presentationId)
            .delete()
    }

    fun generateListItem(presentation: Presentation): PresentationListResponse.ListItem {
        val thumbnailPath = thumbnailResolver.findThumbnail(presentation.id!!)
        return PresentationListResponse.ListItem.from(presentation, thumbnailPath)
    }
}