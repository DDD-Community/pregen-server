package org.kkeunkkeun.pregen.practice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
    "org.kkeunkkeun.pregen.account",
    "org.kkeunkkeun.pregen.common",
    "org.kkeunkkeun.pregen.crypto",
    "org.kkeunkkeun.pregen.practice",
])
class PracticeWebsocketApplication

fun main(args: Array<String>) {
    runApplication<PracticeWebsocketApplication>(*args)
}
