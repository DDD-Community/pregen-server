package org.kkeunkkeun.pregen.account.domain

import jakarta.persistence.*
import org.kkeunkkeun.pregen.common.domain.BaseEntity

@Entity
class Account(
    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var nickName: String,

    var profileImg: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val socialProvider: SocialProvider,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: AccountRole,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
): BaseEntity() {

    fun generatedSocialAccount(email: String, nickName: String, profileImg: String): Account {
        this.email = email
        this.nickName = nickName
        this.profileImg = profileImg
        return this
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }
}