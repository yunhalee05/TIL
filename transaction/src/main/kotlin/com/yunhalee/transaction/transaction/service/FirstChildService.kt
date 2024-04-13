package com.yunhalee.transaction.transaction.service

import com.yunhalee.transaction.transaction.repository.Repository
import com.yunhalee.transaction.transaction.repository.TestEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(noRollbackFor=[RuntimeException::class])
class FirstChildService(
    private val secondChildService: SecondChildService,
    private val repository: Repository
) {
    fun callSecondChild() {
        repository.save(TestEntity(name = "firstChildData"))
        secondChildService.doService()
    }
}