package com.yunhalee.webclient

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun test(@RequestParam("name") name: List<String>, @RequestParam("age") age: List<Int>, @RequestParam("grade") grade: Int): TestSearchData {
        println("-----------------------requested-----------------------")
        println("name: $name")
        println("age: $age")
        println("grade: $grade")
        return TestSearchData(name, age, grade)
    }

    @GetMapping("/test-raw")
    fun testRaw(@RequestParam("name") name: String, @RequestParam("age", required = false) age: Int?, @RequestParam(name = "grade", required = false) grade: Int?): TestData {
        println("-----------------------requested-----------------------")
        println("name: $name")
        println("age: $age")
        println("grade: $grade")
        return TestData(name, age, grade)
    }
}
