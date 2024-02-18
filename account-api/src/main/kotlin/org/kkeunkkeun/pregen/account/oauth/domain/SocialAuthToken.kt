package org.kkeunkkeun.pregen.account.oauth.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
class SocialAuthToken(

    @Column(nullable = false)
    var socialAccessToken: String,

    @Column(nullable = false)
    var socialRefreshToken: String,

    // type datetime
    @Column(nullable = false, columnDefinition = "DATETIME")
    var socialAccessTokenExpiresIn: LocalDateTime,

    @Column(columnDefinition = "DATETIME")
    var socialRefreshTokenExpiresIn: LocalDateTime?,
) {
    fun updateAccessToken(accessToken: String) {
        this.socialAccessToken = accessToken
    }

    fun updateRefreshToken(refreshToken: String) {
        this.socialRefreshToken = refreshToken
    }

    fun updateAccessTokenExpiresIn(expiresIn: Int) {
        this.socialAccessTokenExpiresIn = LocalDateTime.now().plusSeconds(expiresIn.toLong())
    }

    fun updateRefreshTokenExpiresIn(expiresIn: Int) {
        this.socialRefreshTokenExpiresIn = LocalDateTime.now().plusSeconds(expiresIn.toLong())
    }
}
