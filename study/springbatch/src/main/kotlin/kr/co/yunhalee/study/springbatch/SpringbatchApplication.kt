package kr.co.yunhalee.study.springbatch

import kr.co.yunhalee.study.springbatch.infrastructure.FullBeanNameGenerator
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// 5.X 때 부터 해당 어노테이션이 없어야 spring batch job 실행
//@EnableBatchProcessing

// Spring batch repository setting
@EnableBatchProcessing(
//	dataSourceRef = "batchDataSource",
//	transactionManagerRef = "batchTransactionManager",
	tablePrefix = "SYSTEM_",
	maxVarCharLength = 1000,
	isolationLevelForCreate = "ISOLATION_REPEATABLE_READ"
)
@SpringBootApplication(nameGenerator = FullBeanNameGenerator::class)
class SpringbatchApplication

fun main(args: Array<String>) {
	runApplication<SpringbatchApplication>(*args)
}
