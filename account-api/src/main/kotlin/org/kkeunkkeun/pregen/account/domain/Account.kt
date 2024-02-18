package org.kkeunkkeun.pregen.account.domain

import jakarta.persistence.*
import org.kkeunkkeun.pregen.account.domain.dto.OauthTokenResponse
import org.kkeunkkeun.pregen.account.oauth.domain.SocialAuthToken
import org.kkeunkkeun.pregen.common.domain.BaseEntity

@Entity
class Account(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var nickName: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var socialProvider: SocialProvider,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: AccountRole,

    @Embedded
    var socialAuthToken: SocialAuthToken,

    @Column(nullable = false)
    var sessionId: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
): BaseEntity() {

    fun updateAuthProfile(email: String, socialToken: OauthTokenResponse) {
        this.email = email
        this.socialAuthToken.updateAccessToken(socialToken.accessToken)
        this.socialAuthToken.updateAccessTokenExpiresIn(socialToken.expiresIn)
        socialToken.refreshToken?.let { this.socialAuthToken.updateRefreshToken(it) }
        socialToken.refreshTokenExpiresIn?.let { this.socialAuthToken.updateRefreshTokenExpiresIn(it) }
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }
}
