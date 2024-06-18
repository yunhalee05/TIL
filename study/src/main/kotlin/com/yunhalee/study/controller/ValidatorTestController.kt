package com.yunhalee.study.controller

import com.yunhalee.study.controller.request.ValidatorRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/validator")
class ValidatorTestController {

    private val logger = LoggerFactory.getLogger(ValidatorTestController::class.java)

    @PostMapping("/enum")
    fun postEnumTest(
        @Valid @RequestBody
        request: ValidatorRequest
    ) {
        logger.info("request: $request")
        logger.info("request state: ${request.state}")
        // curl -X POST http://localhost:8080/api/v1/validator/enum \
        // -H "Content-Type: application/json" \
        // -d '{
        //  "state": "ACTIVE"
        // }'
    }

    @GetMapping("/enum")
    fun getEnumTest(
        @Validated @ModelAttribute
        request: ValidatorRequest
    ) {
        logger.info("request: $request")
        logger.info("request state: ${request.state}")
        // curl -G "http://localhost:8080/api/v1/validator/enum" \
        //     --data-urlencode "state=active"
    }
}
