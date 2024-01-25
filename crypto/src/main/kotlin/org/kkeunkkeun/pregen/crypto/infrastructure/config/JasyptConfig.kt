package org.kkeunkkeun.pregen.crypto.infrastructure.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig(
    @Value("\${jasypt.encryptor.algorithm}") private val algorithm: String,
    @Value("\${jasypt.encryptor.pool-size}") private val poolSize: Int,
    @Value("\${jasypt.encryptor.string-output-type}") private val stringOutputType: String,
    @Value("\${jasypt.encryptor.key-obtention-iterations}") private val keyObtentionIterations: Int,
    @Value("\${jasypt.encryptor.password}") private val password: String
) {

    @Bean
    fun jasyptStringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        encryptor.setPoolSize(poolSize)
        encryptor.setAlgorithm(algorithm)
        encryptor.setPassword(password)
        encryptor.setStringOutputType(stringOutputType)
        encryptor.setKeyObtentionIterations(keyObtentionIterations)
        return encryptor
    }
}