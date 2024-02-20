package org.kkeunkkeun.pregen.account.service

import org.kkeunkkeun.pregen.account.domain.Account

interface AccountRepository {

    fun findByEmail(email: String): Account?

    fun findIdByEmail(email: String): Long?

    fun findBySessionId(sessionId: String): Account?

    fun save(account: Account): Account

    fun delete(account: Account)
}