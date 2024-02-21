package org.kkeunkkeun.pregen.practice.infrastructure

import org.kkeunkkeun.pregen.practice.presentation.HandshakeFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HandshakeFilterConfiguration(
    private val handshakeFilter: HandshakeFilter
) {

    @Bean
    fun cookieFilter(): FilterRegistrationBean<HandshakeFilter> {
        val registrationBean = FilterRegistrationBean<HandshakeFilter>()

        registrationBean.filter = handshakeFilter
        registrationBean.addUrlPatterns("/ws/info")

        return registrationBean
    }
}
