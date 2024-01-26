package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class JwtTokenUtil(
    private val redisService: RedisService,
    @Value("\${custom.jwt.token.refresh-expiration-time}")
    private var refreshExpirationTime: Long,
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
        redisService.set(newRefreshToken, email, refreshExpirationTime, TimeUnit.MILLISECONDS)
    }

    fun setRefreshToken(refreshToken: String, email: String) {
        redisService.set(refreshToken, email, refreshExpirationTime, TimeUnit.MILLISECONDS)
    }

    fun getRefreshToken(request: HttpServletRequest): String {
        val refreshToken = extractToken(redisService.get(request.getHeader("refreshToken")).toString())
        return refreshToken ?: throw IllegalArgumentException("refreshToken이 존재하지 않습니다.")
    }

    fun deleteRefreshToken(refreshToken: String) {
        redisService.delete(refreshToken)
    }

}
