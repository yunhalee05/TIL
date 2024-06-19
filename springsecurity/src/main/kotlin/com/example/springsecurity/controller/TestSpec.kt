package com.example.springsecurity.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "TestController", description = "TestController API")
interface TestSpec {

    @Tag(name = "test", description = "test")
    @Operation(summary = "test 용", description = "test용 controller입니다.")
    fun test()
}