package org.kkeunkkeun.pregen.common.infrastructure

import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.kkeunkkeun.pregen.common.presentation.PregenException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    private val hashOperations = redisTemplate.opsForHash<String, String>()

    fun set(key: String, value: Any, expiration: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue().set(key, value, expiration, timeUnit)
    }

    fun get(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun updateHashField(sessionId: String, field: String, value: String): String {
        hashOperations.put("practice:$sessionId", field, value)
        return value
    }

    fun getHashField(sessionId: String, field: String): String? {
        return hashOperations.get("practice:$sessionId", field) ?: throw PregenException(ErrorStatus.HASH_FIELD_NOT_FOUND)
    }

    fun getHashTable(sessionId: String): Map<String, String> {
        val entries = hashOperations.entries("practice:$sessionId")

        return if (entries.isEmpty()) {
            throw PregenException(ErrorStatus.HASH_FIELD_NOT_FOUND)
        } else {
            entries
        }
    }

    fun deleteAllFields(sessionId: String) {
        hashOperations.keys("practice:$sessionId").forEach { field ->
            hashOperations.delete("practice:$sessionId", field)
        }
    }
}
