package com.yunhalee.multithread.service

import org.springframework.stereotype.Service

@Service
class ThreadPoolTestService {

    fun test(index : Int) {
        println("start : $index is processing in ${Thread.currentThread()}")
        Thread.sleep(3000)
        println("end : $index is processed in ${Thread.currentThread()}")
    }
}