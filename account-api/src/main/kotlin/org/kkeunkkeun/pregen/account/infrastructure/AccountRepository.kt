package org.kkeunkkeun.pregen.account.infrastructure

import org.kkeunkkeun.pregen.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
}