package org.kkeunkkeun.pregen.common.infrastructure.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(CommonProperties::class)
class AuditingConfig {
}