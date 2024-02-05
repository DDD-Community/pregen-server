package org.kkeunkkeun.pregen.account.infrastructure

import org.kkeunkkeun.pregen.account.domain.Account
import org.kkeunkkeun.pregen.account.service.AccountRepository
import org.springframework.stereotype.Repository
import java.lang.RuntimeException

@Repository
class AccountRepositoryImpl(
    private val accountJpaRepository: AccountJpaRepository
): AccountRepository {

    override fun findByEmail(email: String): Account? {
        return accountJpaRepository.findByEmail(email)
    }

    override fun findIdByEmail(email: String): Long? {
        val account = this.findByEmail(email) ?: throw RuntimeException()
        return account.id
    }
}