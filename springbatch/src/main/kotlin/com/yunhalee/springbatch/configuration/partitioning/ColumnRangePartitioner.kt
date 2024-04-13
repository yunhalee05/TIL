package com.yunhalee.springbatch.configuration.partitioning

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class ColumnRangePartitioner(
    var jdbcTemplate: JdbcOperations? = null,
    var table: String? = null,
    var column: String? = null
) : Partitioner {

    fun setDataSource(dataSource: DataSource) {
        jdbcTemplate = JdbcTemplate(dataSource)
    }

    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val min = jdbcTemplate!!.queryForObject("SELECT MIN($column) from $table", Int::class.java)!!!!
        val max = jdbcTemplate!!.queryForObject("SELECT MAX($column) from $table", Int::class.java)!!!!
        val targetSize = (max - min) / gridSize + 1

        val result: MutableMap<String, ExecutionContext> = HashMap()
        var number = 0
        var start = min
        var end = start + targetSize - 1

        while (start <= max) {
            val value = ExecutionContext()
            result["partition$number"] = value
            if (end >= max) {
                end = max
            }
            value.putInt("minValue", start)
            value.putInt("maxValue", end)
            start += targetSize
            end += targetSize
            number++
        }

        return result
    }
}