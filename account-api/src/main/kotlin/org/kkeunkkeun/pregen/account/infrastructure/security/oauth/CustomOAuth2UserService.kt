package org.kkeunkkeun.pregen.account.infrastructure.security.oauth

import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.service.AccountService
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val redisService: RedisService,
    private val accountService: AccountService,
): OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User = oAuth2UserService.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val randomNickName = accountService.generatedNickName()
        val oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.attributes, randomNickName)

        val memberAttribute: MutableMap<String, Any> = oAuth2Attribute.convertToMap().toMutableMap()
        val email = memberAttribute["email"] as String
        val findAccount = accountService.findByEmail(email)

        findAccount ?: run {
            memberAttribute["exist"] = false
            return DefaultOAuth2User(
                listOf(SimpleGrantedAuthority(AccountRole.MEMBER.value)),
                memberAttribute,
                "email"
            )
        }

        memberAttribute["exist"] = true
        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority(findAccount.role.value)),
            memberAttribute,
            "email"
        )
    }
}