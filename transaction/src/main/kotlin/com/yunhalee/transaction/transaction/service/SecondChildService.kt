package com.yunhalee.transaction.transaction.service

import com.yunhalee.transaction.transaction.repository.Repository
import com.yunhalee.transaction.transaction.repository.TestEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(noRollbackFor=[RuntimeException::class])
class SecondChildService(
    private val repository: Repository
) {

    fun doService() {
        repository.save(TestEntity(name = "secondChildData"))
        throw RuntimeException("예외를 발생시킵니다")
    }
}