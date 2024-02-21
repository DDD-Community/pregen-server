package org.kkeunkkeun.pregen.account.domain

import jakarta.persistence.*
import org.kkeunkkeun.pregen.account.domain.dto.OauthTokenResponse
import org.kkeunkkeun.pregen.account.oauth.domain.SocialAuthToken
import org.kkeunkkeun.pregen.common.domain.BaseEntity
import java.util.*

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
        if (socialToken.refreshToken != null) {
            this.socialAuthToken.updateRefreshToken(socialToken.refreshToken)
            this.socialAuthToken.updateRefreshTokenExpiresIn(socialToken.refreshTokenExpiresIn!!)
        }
    }

    fun updateSocialAuthToken(socialAuthToken: SocialAuthToken) {
        this.socialAuthToken = socialAuthToken
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }

    fun updateSessionId(sessionId: String) {
        this.sessionId = UUID.randomUUID().toString()
    }
}
