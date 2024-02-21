package org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun saveTokenInfo(email: String, accessToken: String, refreshToken: String): RefreshToken {
        refreshTokenRepository.findById(email).ifPresent { refreshTokenRepository.deleteById(email) }
        val savedToken = refreshTokenRepository.save(
            RefreshToken(
                id = email,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        )
        return savedToken
    }

    fun verifyToken(tokenType: String, refreshToken: String): Boolean {
        return when (tokenType) {
            "accessToken" -> refreshTokenRepository.findByAccessToken(refreshToken) != null
            "refreshToken" -> refreshTokenRepository.findByRefreshToken(refreshToken) != null
            else -> false
        }
    }

    fun deleteById(id: String) {
        refreshTokenRepository.deleteById(id)
    }
}