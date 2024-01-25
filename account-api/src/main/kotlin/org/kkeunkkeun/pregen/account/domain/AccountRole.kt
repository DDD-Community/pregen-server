package org.kkeunkkeun.pregen.account.domain

enum class AccountRole(
    val value: String,
) {
    MEMBER("ROLE_MEMBER");

    companion object {
        fun isType(value: String): AccountRole {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid RoleType value: $value")
        }
    }
}