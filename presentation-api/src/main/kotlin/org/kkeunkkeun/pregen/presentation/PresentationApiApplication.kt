package org.kkeunkkeun.pregen.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["org.kkeunkkeun.pregen"])
class PresentationApiApplication

fun main(args: Array<String>) {
    runApplication<PresentationApiApplication>(*args)
}
