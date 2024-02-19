package org.kkeunkkeun.pregen.account.infrastructure.config

import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin { formLogin -> formLogin.disable()}
            .httpBasic { httpBasic -> httpBasic.disable() }
//            .cors { cors -> cors.disable() } // 이후 도메인 설정이 되었다면 변경 필요
            .cors { corsConfigurationSource() } // 이후 도메인 설정이 되었다면 변경 필요
            .csrf { csrf -> csrf.disable() }
            .sessionManagement {
                sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                auth -> auth
                    .requestMatchers(
                        "/api/account/login",
                        "/api/accounts/login/**",
                        "/api/accounts/reissue").permitAll()
                    .anyRequest().authenticated()
            }
            .headers {
                headers -> headers.addHeaderWriter(
                    XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)
                )
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.addAllowedOriginPattern("*")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.validateAllowCredentials()

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/ws/info", configuration)
        return source
    }
}
