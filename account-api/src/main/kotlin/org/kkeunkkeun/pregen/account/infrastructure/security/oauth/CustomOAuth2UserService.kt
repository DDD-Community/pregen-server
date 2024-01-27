package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.domain.dto.NickName
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.kkeunkkeun.pregen.common.service.JsonConvertor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.io.File

@Service
class CustomOAuth2UserService(
    private val redisService: RedisService,
    private val jsonConvertor: JsonConvertor,
): OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // get accessToken in userRequest
        if (redisService.get(userRequest.accessToken.tokenValue) == "BLOCKED") {
            throw IllegalArgumentException("BLOCKED access token")
        }
        val oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User = oAuth2UserService.loadUser(userRequest)

        val provider = userRequest.clientRegistration.registrationId
        val attributeKey = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val randomNickName = generatedNickName(jsonConvertor)
        val oAuth2Attribute = OAuth2Attribute.of(provider, attributeKey, oAuth2User.attributes, randomNickName)
        val memberAttribute: Map<String, Any> = oAuth2Attribute.convertToMap()

        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority(AccountRole.MEMBER.value)),
            memberAttribute,
            "email"
        )
    }

    private fun generatedNickName(jsonConvertor: JsonConvertor): String {
        val jsonContent = File("account-api/src/main/resources/names/names.json").readText()
        val nickName = jsonConvertor.readValue(jsonContent, NickName::class.java)
        return "${nickName.first.random().name} ${nickName.last.random().name}"
    }
}