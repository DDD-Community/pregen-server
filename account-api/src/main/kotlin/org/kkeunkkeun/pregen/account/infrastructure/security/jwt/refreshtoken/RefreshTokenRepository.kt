package org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: CrudRepository<RefreshToken, String> {

    fun findByAccessToken(accessToken: String): RefreshToken?
    fun findByRefreshToken(refreshToken: String): RefreshToken?
}