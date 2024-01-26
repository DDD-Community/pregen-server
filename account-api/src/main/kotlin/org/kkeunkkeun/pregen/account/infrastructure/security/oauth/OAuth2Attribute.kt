package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

class OAuth2Attribute(
    private val attributes: Map<String, Any>,
    private val provider: String,
    private val email: String,
    private val nickName: String,
    private val picture: String,
) {

    companion object {
        fun of(provider: String, attributeKey: String, attributes: Map<String, Any>): OAuth2Attribute {
            return when (provider) {
                "kakao" -> ofKakao(attributeKey, attributes)
                "naver" -> ofNaver(attributeKey, attributes)
                "google" -> ofGoogle(attributeKey, attributes)
                else -> throw IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다.")
            }
        }

        private fun ofKakao(attributeKey: String, attributes: Map<String, Any>): OAuth2Attribute {
            val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
            val profile = kakaoAccount["profile"] as Map<String, Any>

            return OAuth2Attribute(
                attributes = kakaoAccount,
                provider = attributeKey,
                email = kakaoAccount["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                nickName = profile["nickname"] as? String ?: throw IllegalArgumentException("nickname이 존재하지 않습니다."),
                picture = profile["profile_image_url"] as? String ?: throw IllegalArgumentException("profile image가 존재하지 않습니다.")
            )
        }

        private fun ofNaver(attributeKey: String, attributes: Map<String, Any>): OAuth2Attribute {
            val response: Map<String, Any> = attributes["response"] as Map<String, Any>

            return OAuth2Attribute(
                attributes = response,
                provider = attributeKey,
                email = response["email"] as String,
                nickName = response["nickname"] as String,
                picture = response["profile_image"] as String,
            )
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuth2Attribute {
            return OAuth2Attribute(
                attributes = attributes,
                provider = attributeKey,
                email = attributes["email"] as String,
                nickName = attributes["name"] as String,
                picture = attributes["picture"] as String,
            )
        }
    }

    fun convertToMap(): Map<String, Any> {
        return mapOf(
            "provider" to provider,
            "email" to email,
            "nickName" to nickName,
            "picture" to picture,
        )
    }
}
