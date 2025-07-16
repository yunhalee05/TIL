package org.yunhalee.performancetest.mvc_without_virtual_thread

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MvcWithoutVirtualThreadApplication

fun main(args: Array<String>) {
    runApplication<MvcWithoutVirtualThreadApplication>(*args)
}
