package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconsumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaConsumer<T : SpecificRecordBase> {
    fun receive(messages: List<T>, keys: List<String>, partitions: List<Int>, offsets: List<Long>)
}

