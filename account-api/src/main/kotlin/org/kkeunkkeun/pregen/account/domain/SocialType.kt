package org.kkeunkkeun.pregen.account.domain

enum class SocialType(val value: String) {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    companion object {
        fun isType(value: String): SocialType {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("지원하지 않는 소셜 타입입니다.")
        }
    }
}
