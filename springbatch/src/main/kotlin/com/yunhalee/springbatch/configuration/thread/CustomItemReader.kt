package com.yunhalee.springbatch.configuration.thread

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamException

import org.springframework.batch.item.ItemStreamReader

import org.springframework.batch.item.support.ListItemReader

class CustomItemReader<T>(list: List<T>) : ListItemReader<T>(list), ItemStreamReader<T> {
    override fun read(): T? {
        val read = super.read()
        println("Reader :" + read + " => Thread = " + Thread.currentThread().name)
        return read
    }

    @Throws(ItemStreamException::class)
    override fun open(executionContext: ExecutionContext) {
    }

    @Throws(ItemStreamException::class)
    override fun update(executionContext: ExecutionContext) {
    }

    @Throws(ItemStreamException::class)
    override fun close() {
    }
}