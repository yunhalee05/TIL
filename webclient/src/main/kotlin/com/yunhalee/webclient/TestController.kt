package com.yunhalee.webclient

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping
    fun test(@RequestParam("name") name: List<String>, @RequestParam("age") age: List<Int>, @RequestParam("grade") grade: Int) {
        println("-----------------------requested-----------------------")
        println("name: $name")
        println("age: $age")
        println("grade: $grade")
    }

    @GetMapping("/test")
    fun test2() {
        println("-----------------------requested-----------------------")
    }

}