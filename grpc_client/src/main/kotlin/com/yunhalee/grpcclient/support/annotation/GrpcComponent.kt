package com.yunhalee.grpcclient.support.annotation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Documented
@Target(AnnotationTarget.CLASS)
@Retention(RetentionPolicy.RUNTIME)
annotation class GrpcComponent(
    val exception: KClass<out Exception> = Exception::class
)
