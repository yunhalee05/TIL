package com.yunhalee.database.multidatabase.config


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly
import javax.sql.DataSource

class DynamicRoutingDataSource(
    master: DataSource,
    readOnly: DataSource,
) : AbstractRoutingDataSource() {

    init {
        super.setTargetDataSources(
            mapOf(
                DataBaseRole.MASTER to master,
                DataBaseRole.READ_ONLY to readOnly,
            ),
        )
        super.setDefaultTargetDataSource(master)
    }

    override fun determineCurrentLookupKey(): Any =
        when {
            isCurrentTransactionReadOnly() -> DataBaseRole.READ_ONLY
            else -> DataBaseRole.MASTER
        }

    enum class DataBaseRole {
        MASTER,
        READ_ONLY,
    }
}
