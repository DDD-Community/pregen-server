package org.kkeunkkeun.pregen.account.infrastructure

import org.kkeunkkeun.pregen.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository: JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
    fun findBySessionId(sessionId: String): Account?
}