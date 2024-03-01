package org.kkeunkkeun.pregen.common.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "webhook")
data class CommonProperties(
    var url: String,
)