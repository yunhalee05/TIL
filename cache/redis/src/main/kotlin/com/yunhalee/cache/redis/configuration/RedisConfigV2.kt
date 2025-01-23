package com.yunhalee.cache.redis.configuration

//import com.fasterxml.jackson.annotation.JsonInclude
//import com.fasterxml.jackson.databind.DeserializationFeature
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
//import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.KotlinModule
//import org.springframework.cache.annotation.CachingConfigurer
//import org.springframework.cache.annotation.EnableCaching
//import org.springframework.cache.interceptor.CacheErrorHandler
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.cache.RedisCacheConfiguration
//import org.springframework.data.redis.cache.RedisCacheManager
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.RedisSerializationContext
//import java.time.Duration
//
//
//@Configuration
//@EnableCaching
//class RedisConfigV2(
//    private val redisConnectionFactory: RedisConnectionFactory,
//    private val redisErrorHandler: RedisErrorHandler,
//    private val userCacheConfig: UserCacheConfig
//) : CachingConfigurer {
//    companion object {
//        private val DEFAULT_TTL = Duration.ofHours(1)
//
//        private val TEAM_PREFIX_V1 = "team.v1"
//        val TEAM_TTL: Duration = Duration.ofMinutes(10)
//    }
//
//    @Bean
//    override fun cacheManager(): RedisCacheManager {
//        val ptv: PolymorphicTypeValidator =
//            BasicPolymorphicTypeValidator.builder()
//                .allowIfBaseType(Any::class.java)
//                .build()
//
//        val objectMapper =
//            ObjectMapper().apply {
//                registerModule(KotlinModule.Builder().build())
//                registerModule(JavaTimeModule())
//                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                activateDefaultTypingAsProperty(ptv, ObjectMapper.DefaultTyping.NON_FINAL, "@class")
//                setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
//            }
//
//        val serializer = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)
//
//        val defaultCacheConfig =
//            RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                .entryTtl(DEFAULT_TTL)
//
//        val customCacheConfigs = mapOf(
//            userCacheConfig.getCacheConfig(),
//            TEAM_PREFIX_V1 to defaultCacheConfig.entryTtl(TEAM_TTL)
//        )
//        return RedisCacheManager.builder(redisConnectionFactory)
//            .cacheDefaults(defaultCacheConfig)
//            .withInitialCacheConfigurations(customCacheConfigs)
//            .build()
//    }
//
//    override fun errorHandler(): CacheErrorHandler {
//        return redisErrorHandler
//    }
//}