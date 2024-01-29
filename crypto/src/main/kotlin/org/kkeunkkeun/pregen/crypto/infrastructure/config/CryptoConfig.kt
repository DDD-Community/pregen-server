package org.kkeunkkeun.pregen.crypto.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CryptoProperties::class)
class CryptoConfig