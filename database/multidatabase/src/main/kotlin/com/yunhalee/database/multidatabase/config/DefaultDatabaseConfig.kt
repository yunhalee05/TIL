package com.yunhalee.database.multidatabase.config
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DefaultDatabaseConfig {

    @ConfigurationProperties(prefix = "spring.datasources.default.master-datasource.hikari")
    @Bean("masterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @ConfigurationProperties(prefix = "spring.datasources.default.readonly-datasource.hikari")
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
        return DynamicRoutingDataSource(master = masterDataSource, readOnly = readonlyDataSource)
    }

    @Primary
    @Bean
    @ConditionalOnBean(name = ["routingDataSource"])
    fun defaultDataSource(routingDataSource: DataSource): DataSource = LazyConnectionDataSourceProxy(routingDataSource)

    @Bean(name = ["defaultJdbcTemplate"])
    @ConditionalOnBean(name = ["defaultDataSource"])
    fun defaultJdbcTemplate(@Qualifier("defaultDataSource") dataSource: DataSource): JdbcTemplate = JdbcTemplate(dataSource)
}
