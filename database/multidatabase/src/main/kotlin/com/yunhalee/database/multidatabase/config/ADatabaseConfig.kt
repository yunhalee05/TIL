package com.yunhalee.database.multidatabase.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class ADatabaseConfig {
    @ConfigurationProperties(prefix = "spring.datasources.a.master-datasource.hikari")
    @Bean("aMasterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @ConfigurationProperties(prefix = "spring.datasources.a.readonly-datasource.hikari")
    @Bean("aReadonlyDataSource")
    fun readonlyDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("aRoutingDataSource")
    @ConditionalOnBean(name = ["aMasterDataSource", "aReadonlyDataSource"])
    fun routingDataSource(
        @Qualifier("aMasterDataSource") masterDataSource: DataSource,
        @Qualifier("aReadonlyDataSource") readonlyDataSource: DataSource,
    ): DataSource {
        return DynamicRoutingDataSource(master = masterDataSource, readOnly = readonlyDataSource)
    }

    @Bean("aDataSource")
    @ConditionalOnBean(name = ["aRoutingDataSource"])
    fun accountsDataSource(@Qualifier("aRoutingDataSource") aRoutingDataSource: DataSource): DataSource = LazyConnectionDataSourceProxy(aRoutingDataSource)

    @Bean(name = ["aJdbcTemplate"])
    @ConditionalOnBean(name = ["aDataSource"])
    fun accountsJdbcTemplate(@Qualifier("aDataSource") dataSource: DataSource): JdbcTemplate = JdbcTemplate(dataSource)
}
