package com.yunhalee.msa.architecture.common.application

data class ErrorDto(
    val code: String,
    val message: String
) {

    constructor(builder: Builder) : this(
        code = builder.code,
        message = builder.message
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        var code: String = ""
        var message: String = ""

        fun code(code: String) = apply { this.code = code }
        fun message(message: String) = apply { this.message = message }

        fun build() = ErrorDto(this)
    }
}