package kr.co.yunhalee.study.springbatch.configuration.batchconfigurer

import kr.co.yunhalee.study.springbatch.configuration.executioncontext.ExecutionContextConfiguration
import kr.co.yunhalee.study.springbatch.infrastructure.Constants
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

//
//@Configuration
//@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = BatchConfigurationConfiguration.JOB_NAME)
//class CustomBatchConfiguration(
//    private val dataSource: DataSource,
//    private val transactionManager: PlatformTransactionManager
//)  {
//
//
//    @Bean
//     fun jobRepository(): JobRepository {
//
//        val factory = JobRepositoryFactoryBean()
//        factory.setDataSource(dataSource)
//        factory.transactionManager = transactionManager
//        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE") // isolation 수준, 기본값은 “ISOLATION_SERIALIZABLE”
//
//        factory.setTablePrefix("BATCH_") // 테이블 Prefix, 기본값은 “BATCH_”,
//
//        // BATCH_JOB_EXECUTION 가 SYSTEM_JOB_EXECUTION 으로 변경됨
//        // 실제 테이블명이 변경되는 것은 아니다
//        // BATCH_JOB_EXECUTION 가 SYSTEM_JOB_EXECUTION 으로 변경됨
//        // 실제 테이블명이 변경되는 것은 아니다
//        factory.setMaxVarCharLength(1000) // varchar 최대 길이(기본값 2500)
//
//
//        return factory.getObject(); // Proxy 객체가 생성됨 (트랜잭션 Advice 적용 등을 위해 AOP 기술 적용)
//    }
//}