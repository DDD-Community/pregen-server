package org.kkeunkkeun.pregen.account.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties(value = [AccountProperties::class, SocialClientProperties::class])
class AccountConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}