package org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable

@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 15)
data class RefreshToken(

    @Indexed
    var accessToken: String = "",

    var refreshToken: String = "",

    @Id
    var id: String = ""

): Serializable