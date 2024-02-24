package kr.co.yunhalee.study

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionApplication


fun main(args: Array<String>) {
	runApplication<TransactionApplication>(*args)
}
