package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.exception

class KafkaProducerException : RuntimeException{
    constructor(message: String) : super(message)
}