package org.kkeunkkeun.pregen.crypto.infrastructure.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig(
    private val cryptoProperties: CryptoProperties,
) {

    @Bean
    fun jasyptStringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        encryptor.setPoolSize(cryptoProperties.poolSize)
        encryptor.setAlgorithm(cryptoProperties.algorithm)
        encryptor.setPassword(cryptoProperties.password)
        encryptor.setStringOutputType(cryptoProperties.stringOutputType)
        encryptor.setKeyObtentionIterations(cryptoProperties.keyObtentionIterations)
        return encryptor
    }
}