package kr.co.yunhalee.transaction.service

import kr.co.yunhalee.transaction.repository.Entity
import kr.co.yunhalee.transaction.repository.Repository
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
            repository.save(Entity(name = "parentData"))
            firstChildService.callSecondChild()
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }
}