package com.yunhalee.study.service

import com.yunhalee.study.domain.BoardEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class TransactionEventListener {

    val logger: Logger = LoggerFactory.getLogger(TransactionEventListener::class.java)

    @TransactionalEventListener
    fun handleEvent(event: BoardEvent) {
        logger.info("Received board event")
        logger.info("Event type: ${event.eventType}")
        logger.info("Event message: ${event.eventMessage}")
        logger.info("Event board: ${event.board}")
    }
}
