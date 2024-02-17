package org.kkeunkkeun.pregen.account.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "custom")
data class AccountProperties @ConstructorBinding constructor(
    val jwt: JwtProperties,
    val nameJson: String,
) {

    data class JwtProperties(
        val accessExpirationTime: Long,
        val refreshExpirationTime: Long,
        val secret: String,
    )
}