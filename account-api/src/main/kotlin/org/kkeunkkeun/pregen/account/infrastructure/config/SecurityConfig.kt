package org.kkeunkkeun.pregen.account.infrastructure.config

import org.kkeunkkeun.pregen.account.infrastructure.security.oauth.CustomOAuth2UserService
import org.kkeunkkeun.pregen.account.infrastructure.security.oauth.OAuth2LoginFailureHandler
import org.kkeunkkeun.pregen.account.infrastructure.security.oauth.OAuth2LoginSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
    private val oAuth2LoginFailureHandler: OAuth2LoginFailureHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .formLogin { formLogin -> formLogin.disable()}
            .httpBasic { httpBasic -> httpBasic.disable() }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests {
                auth -> auth
                    .requestMatchers("/accounts/**").authenticated()
                    .anyRequest().authenticated()
            }
            .headers {
                headers -> headers.addHeaderWriter(
                    XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)
                )
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .userInfoEndpoint { userInfoEndpoint ->
                        userInfoEndpoint
                            .userService(customOAuth2UserService)
                    }
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler)
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
