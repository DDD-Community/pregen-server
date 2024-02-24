package org.kkeunkkeun.pregen.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = [
    "org.kkeunkkeun.pregen.account",
    "org.kkeunkkeun.pregen.common",
    "org.kkeunkkeun.pregen.crypto",
    "org.kkeunkkeun.pregen.presentation",
])
class PresentationApiApplication

fun main(args: Array<String>) {
    runApplication<PresentationApiApplication>(*args)
}
