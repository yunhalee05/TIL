package com.example.multi_db.config


import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
class DatabaseConfig {
    @ConfigurationProperties(prefix = "spring.master-datasource.hikari")
    @Bean("masterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @ConfigurationProperties(prefix = "spring.readonly-datasource.hikari")
    @Bean("readonlyDataSource")
    fun readonlyDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("routingDataSource")
    @ConditionalOnBean(name = ["masterDataSource", "readonlyDataSource"])
    fun routingDataSource(
        @Qualifier("masterDataSource") masterDataSource: DataSource,
        @Qualifier("readonlyDataSource") readonlyDataSource: DataSource,
    ): DataSource {
        val routingDataSource = DynamicRoutingDataSource()
        val dataSources: Map<Any, Any> = mapOf("master" to masterDataSource, "slave" to readonlyDataSource)
        routingDataSource.setTargetDataSources(dataSources)
        routingDataSource.setDefaultTargetDataSource(masterDataSource)
        return routingDataSource
    }

    @Primary
    @Bean
    @ConditionalOnBean(name = ["routingDataSource"])
    fun dataSource(routingDataSource: DataSource): DataSource = LazyConnectionDataSourceProxy(routingDataSource)
}

internal class DynamicRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any =
        when {
            isCurrentTransactionReadOnly() -> "slave"
            else -> "master"
        }
}