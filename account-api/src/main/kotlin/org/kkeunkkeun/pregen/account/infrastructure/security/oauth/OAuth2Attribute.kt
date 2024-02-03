package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

class OAuth2Attribute(
    private val attributes: Map<String, Any>,
    private val attributeKey: String,
    private val provider: String,
    private val email: String,
    private val nickName: String,
) {

    companion object {
        fun of(provider: String, attributeKey: String, attributes: Map<String, Any>, nickName: String): OAuth2Attribute {
            return when (provider) {
                "kakao" -> ofKakao(provider, "email", attributes, nickName)
                "naver" -> ofNaver(provider, "id", attributes, nickName)
                "google" -> ofGoogle(provider, attributeKey, attributes, nickName)
                else -> throw IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다.")
            }
        }

        private fun ofKakao(provider: String, attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
            val profile = kakaoAccount["profile"] as Map<String, Any>

            return OAuth2Attribute(
                email = kakaoAccount["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                provider = provider,
                attributes = kakaoAccount,
                attributeKey = attributeKey,
                nickName = profile.get("nickname") as? String ?: randomNickName,
            )
        }

        private fun ofNaver(provider: String, attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            val response: Map<String, Any> = attributes["response"] as Map<String, Any>

            return OAuth2Attribute(
                attributes = response,
                provider = provider,
                attributeKey = attributeKey,
                email = response["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                nickName = response["nickname"] as? String ?: randomNickName,
            )
        }

        private fun ofGoogle(provider: String, attributeKey: String, attributes: Map<String, Any>, randomNickName: String): OAuth2Attribute {
            return OAuth2Attribute(
                email = attributes["email"] as? String ?: throw IllegalArgumentException("email이 존재하지 않습니다."),
                provider = provider,
                attributes = attributes,
                attributeKey = attributeKey,
                nickName = attributes["name"] as? String ?: randomNickName,
            )
        }
    }

    fun convertToMap(): Map<String, Any> {
        return mapOf(
            "id" to attributeKey,
            "key" to attributeKey,
            "email" to email,
            "nickName" to nickName,
            "provider" to provider,
        )
    }
}
