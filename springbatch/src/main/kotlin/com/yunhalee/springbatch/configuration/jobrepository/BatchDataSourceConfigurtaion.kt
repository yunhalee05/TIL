package com.yunhalee.springbatch.configuration.jobrepository

import com.zaxxer.hikari.HikariDataSource
import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.batch.BatchDataSource
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = BatchConfigurationConfiguration.JOB_NAME)
@EnableConfigurationProperties(BatchProperties::class)
class BatchDataSourceConfigurtaion {

//    @Bean
//    @BatchDataSource
//    fun batchDataSource(): DataSource {
//        return EmbeddedDatabaseBuilder()
//            .setType(EmbeddedDatabaseType.H2)
//            .addScript("/org/springframework/batch/core/schema-h2.sql")
//            .build()
//    }

    @BatchDataSource
    @ConfigurationProperties(prefix = "spring.datasource.batch.hikari")
    @Bean
    fun batchDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .url("jdbc:h2:mem:testdb2;MODE=MySQL;DB_CLOSE_DELAY=-1")
            .driverClassName("org.h2.Driver")
            .username("sa")
            .password("")
            .build() as HikariDataSource
    }

//    @Bean
//    fun batchTransactionManager(batchDataSource: DataSource): PlatformTransactionManager {
//        return JdbcTransactionManager(batchDataSource)
//    }

    @Bean
    fun batchTransactionManager(batchDataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(batchDataSource)
    }


    //jdbc
//    @Bean
//    fun batchTransactionManager(batchDataSource: DataSource): JdbcTransactionManager {
//        return JdbcTransactionManager(batchDataSource)
//    }
//
//    @Bean
//    fun batchJdbcTemplate(batchDataSource: DataSource): JdbcTemplate {
//        return JdbcTemplate(batchDataSource)
//    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher, jobExplorer: JobExplorer,
        jobRepository: JobRepository, properties: BatchProperties
    ): JobLauncherApplicationRunner {
        val runner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
        val jobName: String? = properties.job.name
        if (!jobName.isNullOrEmpty()) {
            runner.setJobName(jobName)
        }
        return runner
    }


//
//
//    이유를 확인해 보니 Spring Boot Auto Configure에서 배치가 있으면 실행할 수 있도록 로직이 되어있는데 Spring Boot 3.0부터 DefaultBatchConfiguration 클래스나 EnableBatchProcessing 어노테이션을 선언할 경우 자동 실행 하는 것을 막는 로직이 생겨버렸네요.
//
//    org.spring.boot.autoconfigure.btach.BatchAutoConfiguration 참고
//    @ConditionalOnMissingBean이 추가되어 DefaultBatchConfiguration, EnableBatchProcessing이 있을 경우 실행에 필요한 JobLauncherApplicationRunner도 생성이 되지 않습니다.
//
//    @AutoConfiguration(after = { HibernateJpaAutoConfiguration.class, TransactionAutoConfiguration.class })
//    @ConditionalOnClass({ JobLauncher.class, DataSource.class, DatabasePopulator.class })
//    @ConditionalOnBean({ DataSource.class, PlatformTransactionManager.class })
//    @ConditionalOnMissingBean(value = DefaultBatchConfiguration.class, annotation = EnableBatchProcessing.class)
//    @EnableConfigurationProperties(BatchProperties.class)
//        @Import(DatabaseInitializationDependencyConfigurer.class)
//            public class BatchAutoConfiguration {
//            @Bean
//            @ConditionalOnMissingBean
//            @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
//            public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
//            JobRepository jobRepository, BatchProperties properties) {
//                JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
//                String jobNames = properties.getJob().getName();
//                if (StringUtils.hasText(jobNames)) {
//                    runner.setJobName(jobNames);
//                }
//                return runner;
//            }
//            ...
//        }


//    @Bean
//    @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer::class)
//    fun batchDataSourceInitializer(dataSource: DataSource?, @BatchDataSource batchDataSource: ObjectProvider<DataSource?>, properties: BatchProperties): BatchDataSourceScriptDatabaseInitializer {
//        return BatchDataSourceScriptDatabaseInitializer(batchDataSource.getIfAvailable { dataSource }, properties.jdbc)
//    }
//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
//    fun batchDataSourceInitializer(
//        batchDataSource: DataSource?,
//        properties: BatchProperties
//    ): BatchDataSourceScriptDatabaseInitializer {
//        return BatchDataSourceScriptDatabaseInitializer(batchDataSource, properties.jdbc)
//    }

    // batchDatasource 사용을 위한 수동 빈 등록
    @Bean
    @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer::class)
    fun batchDataSourceInitializer(dataSource: DataSource?, @BatchDataSource batchDataSource: ObjectProvider<DataSource?>, properties: BatchProperties): BatchDataSourceScriptDatabaseInitializer {
        return BatchDataSourceScriptDatabaseInitializer(batchDataSource.getIfAvailable { dataSource }, properties.jdbc)
    }


//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
//    fun jobExplorer(@BatchDataSource batchDataSource: DataSource): JobExplorer {
//        val factory = JobExplorerFactoryBean()
//        factory.setDataSource(batchDataSource)
//        factory.setTablePrefix("SYSTEM_")
//        factory.afterPropertiesSet();
//        return factory.`object`
//    }
//

}