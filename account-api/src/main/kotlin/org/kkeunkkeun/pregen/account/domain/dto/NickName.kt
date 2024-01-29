package org.kkeunkkeun.pregen.account.domain.dto

data class NickName(
    val first: MutableList<First>,
    val last: MutableList<Last>,
)

data class First(
    val name: String,
)

data class Last(
    val name: String,
)