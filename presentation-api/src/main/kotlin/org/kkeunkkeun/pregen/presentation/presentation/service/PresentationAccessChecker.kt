package org.kkeunkkeun.pregen.presentation.presentation.service

import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.kkeunkkeun.pregen.presentation.presentation.domain.Presentation
import org.springframework.stereotype.Service

/**
 * Presentation 접근 권한을 확인한다.
 *
 * TODO: 권한(MEMBER, ADMIN, ...)에 따른 확인 로직 추가
 */
@Service
class PresentationAccessChecker(
    private val accountRepository: AccountRepository,
) {

    fun checkAccess(presentation: Presentation, email: String) {
        val accountId = accountRepository.findIdByEmail(email)
            ?: throw RuntimeException()

        presentation.checkDeleted()

        if (presentation.isNotOwnerOfPresentation(accountId)) {
            throw RuntimeException() // TODO
        }
    }
}