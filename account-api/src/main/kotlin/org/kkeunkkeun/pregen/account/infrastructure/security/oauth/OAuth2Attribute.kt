package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

class OAuth2Attribute(
    private val attributes: Map<String, Any>,
    private val provider: String,
    private val email: String,
    private val nickName: String,
) {

    companion object {
        fun of(provider: String, attributeKey: String, attributes: Map<String, Any>, nickName: String): OAuth2Attribute {
            return when (provider) {
                "kakao" -> ofKakao(attributeKey, attributes, nickName)
                "naver" -> ofNaver(attributeKey, attributes, nickName)
                "google" -> ofGoogle(attributeKey, attributes, nickName)
                else -> throw IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다.")
            }
        }

        private fun ofKakao(attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
            val profile = kakaoAccount["profile"] as? Map<String, Any> ?: randomNickName

            return OAuth2Attribute(
                attributes = kakaoAccount,
                provider = attributeKey,
                email = kakaoAccount["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                nickName = randomNickName,
            )
        }

        private fun ofNaver(attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            val response: Map<String, Any> = attributes["response"] as Map<String, Any>

            return OAuth2Attribute(
                attributes = response,
                provider = attributeKey,
                email = response["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                nickName = response["nickname"] as String? ?: randomNickName,
            )
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            return OAuth2Attribute(
                attributes = attributes,
                provider = attributeKey,
                email = attributes["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                nickName = attributes["name"] as? String ?: randomNickName,
            )
        }
    }

    fun convertToMap(): Map<String, Any> {
        return mapOf(
            "provider" to provider,
            "email" to email,
            "nickName" to nickName,
        )
    }
}
