package org.kkeunkkeun.pregen.account.domain

enum class SocialProvider(val value: String) {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    companion object {
        fun isType(value: String): SocialProvider {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid SocialProvider value: $value")
        }
    }
}
