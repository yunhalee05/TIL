package com.yunhalee.springbatch.configuration.chunk

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamException
import org.springframework.batch.item.ItemStreamReader


class StreamReader(
    var items: List<String> = listOf(),
    var index: Int = 0
) : ItemStreamReader<String> {

    private var restart = false


    @Throws(Exception::class)
    override fun read(): String? {
        var item: String? = null
        if (index < items.size) {
            item = items[index]
            index++
        }
        if (index == 8 && !restart) {
            throw RuntimeException("Restart is required.")
        }
        return item
    }

    @Throws(ItemStreamException::class)
    override fun open(executionContext: ExecutionContext) {
        if (executionContext.containsKey("index")) {
            index = executionContext.getInt("index")
            restart = true
        } else {
            index = 0
            executionContext.put("index", index)
        }
    }

    @Throws(ItemStreamException::class)
    override fun update(executionContext: ExecutionContext) {
        executionContext.put("index", index)
    }

    @Throws(ItemStreamException::class)
    override fun close() {
        println("close")
    }
}