package com.yunhalee.multithread.util

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadBuilder {

    companion object {
        fun fixed(threadPoolSize: Int) = ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue())

        fun cached(minThreadPoolSize: Int? = 0, maxThreadPoolSize: Int? = Int.MAX_VALUE) = ThreadPoolExecutor(minThreadPoolSize!!, maxThreadPoolSize!!, 60L, TimeUnit.SECONDS, SynchronousQueue());

    }
}