package org.kkeunkkeun.pregen.common.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CommonProperties::class)
class CommonConfig