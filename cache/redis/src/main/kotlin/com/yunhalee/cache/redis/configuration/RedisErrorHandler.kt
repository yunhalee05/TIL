package com.yunhalee.cache.redis.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.stereotype.Component

@Component
class RedisErrorHandler : CacheErrorHandler {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handleCacheGetError(exception: java.lang.RuntimeException, cache: Cache, key: Any) {
        logger.error("레디스 예외로 ${cache.name}::$key 조회 실패, 예외 : $exception")
    }

    override fun handleCachePutError(exception: java.lang.RuntimeException, cache: Cache, key: Any, value: Any?) {
        logger.error("레디스 예외로 ${cache.name}::$key 교체 실패, 예외 : $exception")
    }

    override fun handleCacheEvictError(exception: java.lang.RuntimeException, cache: Cache, key: Any) {
        logger.error("레디스 예외로 ${cache.name}::$key 삭제 실패, 예외 : $exception")
    }

    override fun handleCacheClearError(exception: java.lang.RuntimeException, cache: Cache) {
        logger.error("레디스 예외로 ${cache.name} 삭제 실패, 예외 : $exception")
    }
}
