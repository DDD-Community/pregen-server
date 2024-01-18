package org.kkeunkkeun.pregen.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PresentationApiApplication

fun main(args: Array<String>) {
    runApplication<AccountApiApplication>(*args)
}
