package kr.co.yunhalee.transaction.service

import kr.co.yunhalee.transaction.repository.Entity
import kr.co.yunhalee.transaction.repository.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(noRollbackFor=[RuntimeException::class])
class FirstChildService(
    private val secondChildService: SecondChildService,
    private val repository: Repository
) {
    fun callSecondChild() {
        repository.save(Entity(name = "firstChildData"))
        secondChildService.doService()
    }
}