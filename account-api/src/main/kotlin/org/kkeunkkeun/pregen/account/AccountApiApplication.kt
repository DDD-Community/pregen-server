package org.kkeunkkeun.pregen.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
    "org.kkeunkkeun.pregen.common",
    "org.kkeunkkeun.pregen.crypto",
    "org.kkeunkkeun.pregen.account",
])
class AccountApiApplication

fun main(args: Array<String>) {
    runApplication<AccountApiApplication>(*args)
}
