package org.kkeunkkeun.pregen.account.infrastructure

import org.kkeunkkeun.pregen.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AccountRepository: JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>
}