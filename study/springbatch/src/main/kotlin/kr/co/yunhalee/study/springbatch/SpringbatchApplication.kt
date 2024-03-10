package kr.co.yunhalee.study.springbatch

import kr.co.yunhalee.study.springbatch.infrastructure.FullBeanNameGenerator
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.runApplication

// 5.X 때 부터 해당 어노테이션이 없어야 spring batch job 실행
//@EnableBatchProcessing
@SpringBootApplication(nameGenerator = FullBeanNameGenerator::class)
class SpringbatchApplication

fun main(args: Array<String>) {
    runApplication<SpringbatchApplication>(*args)
}
