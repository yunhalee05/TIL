package kr.co.yunhalee.transaction.service

import kr.co.yunhalee.transaction.repository.Entity
import kr.co.yunhalee.transaction.repository.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(noRollbackFor=[RuntimeException::class])
class SecondChildService(
    private val repository: Repository
) {

    fun doService() {
        repository.save(Entity(name = "secondChildData"))
        throw RuntimeException("예외를 발생시킵니다")
    }
}