package com.yunhalee.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toInstant(): Instant = this.atZone(ZoneId.of("Asia/Seoul")).toInstant()
fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Asia/Seoul"))
