package com.yunhalee.transaction.transaction.service

import com.yunhalee.transaction.transaction.repository.Repository
import com.yunhalee.transaction.transaction.repository.TestEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ParentService(
    private val firstChildService: FirstChildService,
    private val repository: Repository
) {

    fun callFirstChild() {
        try {
            repository.save(TestEntity(name = "parentData"))
            firstChildService.callSecondChild()
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }
}