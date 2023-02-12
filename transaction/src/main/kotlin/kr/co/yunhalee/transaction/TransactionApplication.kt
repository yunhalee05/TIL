package kr.co.yunhalee.transaction

import kr.co.yunhalee.transaction.service.ParentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionApplication


fun main(args: Array<String>) {
	runApplication<TransactionApplication>(*args)
}
