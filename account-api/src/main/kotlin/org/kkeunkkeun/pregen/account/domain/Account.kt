package org.kkeunkkeun.pregen.account.domain

import jakarta.persistence.*
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

    @Column(nullable = false)
    var socialAccessToken: String,

    @Column(nullable = false)
    var sessionId: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
): BaseEntity() {

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }

    fun updateAccessToken(accessToken: String) {
        this.socialAccessToken = accessToken
    }
}