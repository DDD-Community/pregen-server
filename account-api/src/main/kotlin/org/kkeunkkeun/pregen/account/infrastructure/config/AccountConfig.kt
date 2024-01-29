package org.kkeunkkeun.pregen.account.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AccountProperties::class)
class AccountConfig