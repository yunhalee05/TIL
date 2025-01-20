package com.yunhalee.database.multidatabase.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class QueryDslConfig {

    @PersistenceContext(unitName = "default")
    private val defaultEntityManager: EntityManager? = null

    @PersistenceContext(unitName = "a")
    private val aEntityManager: EntityManager? = null

    @Primary
    @Bean
    fun defaultQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(defaultEntityManager)
    }

    @Bean
    fun aQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(aEntityManager)
    }
}
