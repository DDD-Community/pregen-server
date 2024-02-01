package org.kkeunkkeun.pregen.crypto.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "jasypt.encryptor")
data class CryptoProperties @ConstructorBinding constructor(
    val algorithm: String,
    val poolSize: Int,
    val stringOutputType: String,
    val keyObtentionIterations: Int,
    val password: String
)