package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class JwtTokenUtil(
    private val redisService: RedisService,
    private val accountProperties: AccountProperties,
) {

    companion object {
        fun extractToken(token: String?): String? {
            if (token != null) {
                if (!token.startsWith("Bearer ")) {
                    throw IllegalArgumentException("Invalid token")
                }
                return token.split(" ")[1].trim()
            }
            return null;
        }
    }

    fun rotateRefreshToken(prevRefreshToken: String, newRefreshToken: String, email: String) {
        redisService.delete(prevRefreshToken)
        redisService.set(newRefreshToken, email, accountProperties.jwt.refreshExpirationTime, TimeUnit.MILLISECONDS)
    }

    fun setRefreshToken(refreshToken: String, email: String) {
        redisService.set(refreshToken, email, accountProperties.jwt.refreshExpirationTime, TimeUnit.MILLISECONDS)
    }

    fun getAccessToken(request: HttpServletRequest): String {
        val accessToken = extractToken(request.getHeader("accessToken"))
        return accessToken ?: throw IllegalArgumentException("accessToken이 존재하지 않습니다.")
    }

    fun getRefreshToken(request: HttpServletRequest): String {
        val refreshToken = extractToken(redisService.get(request.getHeader("refreshToken")).toString())
        return refreshToken ?: throw IllegalArgumentException("refreshToken이 존재하지 않습니다.")
    }

    fun blockAccessToken(accessToken: String) {
        redisService.set(accessToken, "BLOCKED", accountProperties.jwt.accessExpirationTime, TimeUnit.MILLISECONDS)
    }

    fun deleteRefreshToken(refreshToken: String) {
        redisService.delete(refreshToken)
    }

}